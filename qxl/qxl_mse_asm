; QXL_MSE.ASM Mouse server and setup and restore

; check which COM port is used by the mouse (if any)
; and link in mouse driver
; 2005.01.10    1.01    correct saves (BC)
; 2006.10.01	1.02	bugfix (BC)

qxl_mse_setup:
        ASSUME ds:DGROUP
        push    es
        xor     ax,ax
        mov     es,ax
        ASSUME es:BASESEG
        mov     ax,es:int_msei
        or      ax,es:int_mses   ; any mouse handler?
        pop     es
        jz      qms_ret          ; ... no

        ASSUME es:DGROUP

        mov     ax,024h          ; info
        int     33h

        mov     al,080h          ; assume bus etc
        cmp     ch,2                     ; serial?
        jne     qms_save                 ; ... no
        sub     cl,4
        neg     cl                       ; 0 (COM1, IRQ4) or 1 (COM2, IRQ3)
        mov     al,cl

qms_save:
        mov     mse_port,al              ; save port number

        push    es
        mov     ax,015h          ; storage needs
        int     33h
        shr     bx,4                     ; in paras
        inc     bx
        mov     ah,048h          ; allocate
        int     21h
        mov     mse_save+6,ax    ; save segment
        mov     es,ax
        xor     dx,dx                    ; store at 0
        mov     ax,016h                  ; store state
        int     33h

        mov     cx,0ffh          ; interrupt on button or move
        mov     ax,cs
        mov     es,ax
        mov     dx, OFFSET cs:qxl_mse_server
        mov     ax,014h                  ; swap mouse int servers
        int     33h
        mov     mse_save,cx              ; store old values
        mov     mse_save+2,dx
        mov     mse_save+4,es
        pop     es
; push ax
; mov   si, sp
; mov   di, OFFSET mse_port
; call  deb_cnt_buff
; pop  ax

        mov     dx,07fffh                ; high double speed threshold -1.02 bugfix
        mov     ax,013h
        int     33h

        mov     cx,010h          ; half speed
        mov     dx,cx
        mov     ax,0fh
        int     33h

; Dummy mouse reset

qxl_mse_reset:

qms_ret:
        retn

; restore mouse driver

qxl_mse_restore:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

        cmp     mse_port,0ffh    ; any mouse handler?
        jz      qmr_ret          ; ... no

        push    es
        mov     cx,mse_save              ; restore values
        mov     dx,mse_save+2
        mov     es,mse_save+4
        mov     ax,014h                  ; swap mouse int servers
        int     33h

        mov     es, mse_save+6   ; state buffer segment
        xor     dx,dx
        mov     ax,017h          ; restore state
        int     33h
; push ax
; mov   si, sp
; mov   di, OFFSET mse_port
; call  deb_cnt_buff
; pop  ax
        pop     es
qmr_ret:
        retn

; the mouse server itself

qxl_mse_server:
        push    ds
        push    es
        push    ax
        push    di
        push    si
        cld
        mov     ax, DGROUP
        mov     ds,ax
        mov     es,ax
        ASSUME ds:DGROUP
        ASSUME es:DGROUP
        mov     ds:mse_button,bl        ; mouse buttons
        sar     si,1
;;;     sar     si,1
        sar     di,1
;;;     sar     di,1
        sub     si,ds:mse_sx
        add     ds:mse_sx,si
        mov     ax,si
        xchg    al,ah
        mov     ds:mse_dx,ax    ; x move
        sub     di,ds:mse_sy
        add     ds:mse_sy,di
        mov     ax,di
        xchg    al,ah
        mov     ds:mse_dy,ax    ; y move

        mov     cx,8/2                  ; 8 byte message
        mov     ax, OFFSET mse_mlen
; mov   si, OFFSET mse_mcmd
; mov   di, OFFSET mse_sx
; call deb_cnt_buff
        call    qxl_smess                       ; send message
;;;     jc      qms_smess ; if it does not go, never mind

        pop     si
        pop     di
        pop     ax
        pop     es
        pop     ds
        retf

