; QXL_COMP.ASM Communications port server and setup
; check which COM ports are available (for the moment 1-4 only) and set them up
; 2006.10.01	1.01	various bug fixes (BC)
;			at qxl_comp_server_i4: instructions placed before the iret
;			at qcd_ploop: test on buffer added (so that the test on com_txdata in QXL_MAIN.ASM is useful again
;			com_txdata can onlty be 0 or -1

qxl_comp_setup:
        ASSUME ds:DGROUP
        push    es
        xor     ax,ax
        mov     es,ax
        ASSUME es:BASESEG

        mov     bx, OFFSET qxl_combuff
        mov     cx,-1
        mov     dx,cx
qcs_loop:
        inc     cx                       ; next COM
        cmp     cx,4                     ; all done?
        je      qcs_done

        mov     bp,cx
        add     bp,bp                    ; index tables

        mov     ax,es:[bp+add_com1] ; address of COMn
        test    ax,ax
        jz      qcs_loop                 ; ... none

        cmp     cl, mse_port     ; used for mouse?
        jz      qcs_loop                 ; ... yes

        mov     [bp+com_n],ax    ; set address
        mov     dx,cx                    ; highest port

        mov   [bp+com_ibuff],bx  ; input buffer address
        add     bx,qxl_buffalloc

        mov   [bp+com_obuff],bx  ; and output buffer address
        add     bx,qxl_buffalloc

        mov     ax,1                     ; set bit
        shl     ax,cl
        xchg    ah,al                    ; big-endian
        or      mes1_com,ax              ; mark in setup message
        or    flowpc_com, ax     ; and in flow control
        or    flowqx_com, ax
        jmp     qcs_loop

qcs_done:
        pop     es
        ASSUME es:DGROUP

        inc     dx
        test    dx,dx                    ; highest port number 1 to n
        jz      qcs_exit
        mov     com_maxport,dx

; mov si, OFFSET com_n
; call deb_wbuff

qcs_inst_irq4:
        mov     ax, com_n                ; com1
        or      ax, com_n+4              ; com3
        jz      qcs_inst_irq3    ; neither

        mov     dx,pic_mask
        in      al,dx
        and     al,0efh          ; enable irq4 (bit 4)
        out     dx,al

        mov     si, OFFSET int_rq4i

; mov al,"4"
; call deb_wchr
        test    mse_port, 081h   ; is this a mouse port?
        jz      qcs_inst_irq4c   ; ... yes

; mov al,"r"
; call deb_wchr
        mov     cx, OFFSET qxl_comp_server_i4
        call    qxl_setvr                ; set up server
        jmp     qcs_inst_irq3

qcs_inst_irq4c:
; mov al,"v"
; call deb_wchr
        mov     cx, OFFSET qxl_comp_server_i4c
        mov     bp, OFFSET qci4_patch4-4
        call    qxl_setvc                ; set up server to continue


qcs_inst_irq3:
        mov     ax, com_n+2             ; com2
        or      ax, com_n+6             ; com4
        jz      qcs_inst_irq_done ; ... neither

        mov     dx,pic_mask
        in      al,dx
        and     al,0f7h          ; enable irq3 (bit 3)
        out     dx,al

        mov     si, OFFSET int_rq3i
; mov al,"3"
; call deb_wchr

        test    mse_port, 081h   ; is this a mouse port?
        jg      qcs_inst_irq3c   ; ... yes

; mov al,"r"
; call deb_wchr
        mov     cx, OFFSET qxl_comp_server_i3
        call    qxl_setvr                ; set up server
        jmp     qcs_inst_irq_done

qcs_inst_irq3c:
; mov al,"c"
; call deb_wchr
        mov     cx, OFFSET qxl_comp_server_i3c
        mov     bp, OFFSET qci3_patch4-4
        call    qxl_setvc                ; set up server to continue

qcs_inst_irq_done:
; mov al,00ah
; call deb_wchr

;
; COM reset (set) ports
;
qxl_comp_reset:
        mov     com_notbusy,-1
        mov     com_txdata,0
        mov     cx,com_maxport

qcs_resetp:
        mov   bp,cx
        dec     bp
        add     bp,bp
        mov     bx,[bp+com_ibuff]
        test    bx,bx
        jz      qcs_rseloop
        mov   ax,qxl_buffsize
        call    buff_set                ; set up input buffer

        mov     bx,[bp+com_obuff]
        mov   ax,qxl_buffsize
        call    buff_set                ; set up output buffer
        mov     [bp+com_room],ax        ; room in buffer

        mov     dx,[bp+com_n]

        add     dl,com_ier               ; enable interrupts
        mov     al,com_ire               ; receive interrupt enable
        out     dx,al

        add     dl,com_fic-com_ier
        mov     al,com_fiz               ; enable and clear 0 threshold fifo
        out     dx,al
        out     dx,al

        ;add    dl,com_iir-com_fic=0!!
        in      al,dx                    ; clear int ID
        sub     dl,com_iir-com_data
        in      al,dx                    ; clear receive data
        add     dl,com_msr-com_data
        in      al,dx                    ; and modem status

        sub     dl,com_msr-com_mcr
        mov     al,com_nrts              ; set no RTS
        out     dx,al

        sub     dl,com_mcr-com_lcr
        mov     al,com_dlab
        out     dx,al
        sub     dl,com_lcr
        mov     al,3
        out     dx,al                    ; set baud rate
        inc     dl
        xor     al,al
        out     dx,al
        add     dl,com_lcr-1
        mov     al,com_lcr8
        out     dx,al                    ; reset line reg

qcs_rseloop:
        loop    qcs_resetp

qcs_exit:
        retn

;
; COM restore ports - no state was saved, cannot restore
;
qxl_comp_restore:
      retn

;
; COM set routine (ax is baud rate divisor, bx is port number * 2)
;
qxl_comp_set:
        mov     cx,ax
        mov     dx,[bx+com_n]    ; set address
        add     dl,com_lcr
        mov     al,com_dlab
        out     dx,al
        sub     dl,com_lcr
        mov     al,cl
        out     dx,al                    ; set baud rate LSB
        inc     dl
        mov     al,ch
        out     dx,al                    ; ... and msb
        add     dl,com_lcr-1
        mov     al,com_lcr8
        out     dx,al                    ; reset line reg
        jmp     qxl_rxm_loop

;
; COM open routine (al is mode, bx is port number * 2)
;
qxl_comp_open:
        mov     dx,[bx+com_n]    ; set address
        mov     BYTE PTR [bx+com_rxoff],0
        add     dl,com_mcr
        mov     al,com_erts              ; DTR and RTS
        out     dx,al
        jmp     qxl_rxm_loop

;
; COM close routine (al is mode, bx is port number * 2)
;
qxl_comp_close:
        mov     dx,[bx+com_n]    ; set address
        mov     BYTE PTR [bx+com_rxoff],-1
        add     dl,com_mcr
        mov     al,com_nrts              ; no RTS
        out     dx,al
        jmp     qxl_rxm_loop

;
; COM transmit routine (ax is number of bytes, bx is port number * 2)
;
qxl_comp_tx:
        mov     bp,bx

        mov     cx,ax
        add     ax,3                     ; round up
        and     al,0fch
        add     ax,si                    ; next message
        push    ax

        mov     bx,[bp+com_obuff]       ; buffer
        test    bx,bx
        jz      qct_done

qct_loop:
        lodsb
        call    buff_pbyte               ; save data
        jz      qct_stop
        dec     [bp+com_room]    ; less room
        loop    qct_loop

        cmp     [bp+com_room],qxl_buffmin
        jg      qct_data

qct_stop:
        mov     ax,-2
        mov     cx,bp
        shr     cx,1
        shl     ax,cl
        xchg    ah,al
        and     flowpc_com,ax    ; clear handshake bit
        mov     flowpc_head,flowpc_len ; and send message

qct_data:
        mov     com_txdata,-1            ; say that there is data in queue

qct_done:
        pop     si                       ; next message
        jmp     qxl_rxm_loop

; COM receive interrupt server (the transmit is direct!)

qxl_comp_server_i3c:
        call    qxl_comp_server
        jmp     FAR PTR int_rq3i
qci3_patch4:

qxl_comp_server_i4c:
        call    qxl_comp_server
        jmp     FAR PTR int_rq4i
qci4_patch4:

qxl_comp_server_i3:
qxl_comp_server_i4:
        call    qxl_comp_server
        push    ax
        mov     al,pic_eof              ; end of interrupt
        out     pic_ctrl,al
        pop     ax
        iret

qxl_comp_server:
        push    ds
        push    es
        push    ax
        push    bx
        push    cx
        push    dx
        push    di

        mov     ax, DGROUP
        mov     ds,ax
        mov     es,ax
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

        mov     cx, com_maxport

; mov al,ch
; call deb_whex
; mov al,cl
; call deb_whex

        test    cx,cx                           ; any ports?
        jz      qci_done

; push si
; mov si, OFFSET com_n
; call deb_wbuff
; pop si
; mov al,00ah
; call deb_wchr

qci_ploop:
; mov al,cl
; add al,010h
; call deb_whex
        mov     di,cx
      dec       di                              ; internally, ports are from 0 - maxport-1
        add     di,di
        mov     dx,[di+com_n]           ; any com port?
; mov  al,dh
; call deb_whex
; mov  al,dl
; call deb_whex
        test    dx,dx
        jz      qci_eploop
; mov al,cl
; add al,020h
; call deb_whex

; add   dl,com_iir
; in    al,dx                   ; int ident
; sub   dl,com_iir
; call deb_whex

qci_bloop:
        add     dl,com_lsr-com_data ; line status
        in      al,dx
; call deb_whex
        test    al,com_rdr              ; rx byte
        jz      qci_eploop              ; ... no
; mov al,cl
; add al,030h
; call deb_whex
        sub     dl,com_lsr-com_data
        in      al,dx                   ; rx data
; call deb_whex
        mov     bx, [di+com_ibuff]
        call    buff_pbroom             ; put byte and check room
        mov     [di+com_rxdata],1
        cmp     ax,qxl_buffrts  ; enough spare
        ja      qci_bloop               ; ... yes
        add     dl,com_mcr-com_data
        mov     al,com_nrts     ; negate rts
        out     dx,al
        sub     dl,com_mcr-com_data
        or      BYTE PTR [di+com_rxoff],1 ; off (or closed)

        jmp     qci_bloop

qci_eploop:
        loop    qci_ploop

; mov al,00ah
; call deb_wchr

qci_done:
        pop     di
        pop     dx
        pop     cx
        pop     bx
        pop     ax
        pop     es
        pop     ds
        retn

; COM transmit (direct, called from main loop to transfer data from buffer)

qxl_do_comp:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

        xor     ax,ax
        mov     com_txdata,al           ; say data taken (we may reset this later)

        mov     cx, com_maxport
        test    cx,cx                           ; any ports?
        jz      qcd_done

qcd_ploop:
        mov     bp,cx
        dec     bp              ; internally, ports are from 0 - maxport-1
        add     bp,bp
        mov     dx,[bp+com_n]           ; any com port?
        test    dx,dx
        jz      qcd_eploop
        mov     bx,[bp+com_obuff]
        call    buff_test
        jz      qcd_eploop
        add     dl,com_lsr              ; read port status
        in      al,dx
        test    al,com_thr                    ; transmitter holding reg empty?
        jz      qcd_nready                    ; ... no
        inc     dl                              ; com_msr = com_lsr+1
        in      al,dx
        test    al,com_cts                      ; cts?
        jz      qcd_nready
        call    buff_gbyte                      ; get data byte
        call    buff_test
        setnz   ah
        neg     ah
        or      com_txdata,ah
        inc   [bp+com_room]
        sub     dl,com_msr-com_data
        out     dx,al                         ; send data byte
        jmp     qcd_ndata

qcd_nready:
        mov     com_txdata,-1                   ; there is still data to go

qcd_ndata:
        cmp     [bp+com_room],qxl_buffmin
        jle     qcd_eploop
        mov     ax,1
        dec     cl
        shl     ax,cl
        inc     cl
        xchg    ah,al
        test    flowpc_com,ax
        jnz     qcd_eploop
        or      flowpc_com,ax    ; set handshake bit
        mov     flowpc_head,flowpc_len ; and send message

qcd_eploop:
        loop    qcd_ploop

qcd_done:
        retn
