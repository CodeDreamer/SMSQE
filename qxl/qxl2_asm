; QXL2.ASM Simple QXL Loader / Support Program
; 2004.01.01            v. 1.01         added code to support configured drives (BC)
; 2005.01.10            v. 1.02         needs at least a 286
; 2005.10.01            v. 1.03         needs at least a 386 (BC)

        .386
        OPTION SEGMENT:USE16

        INCLUDE QXL.INC

DGROUP  GROUP   _data,stack,_bss

_text   SEGMENT DWORD PUBLIC 'CODE'
start:
        pushf
        mov     ax,3002h
        push    ax
        popf
        pushf
        pop     bx
        popf
        cmp     bx,ax           ; 386+?
        jz      min386          ; ... yes
        push    cs
        pop     ds
        ASSUME ds:_text
        mov     dx, OFFSET max286
        mov     ah,09h                          ; Display String
        int     21h
        jmp     ex_exit

max286:
        db      '386+ required','$'

min386:
        mov     bx,es
        mov     ax,DGROUP
        mov     es,ax
        ASSUME es:DGROUP
        mov     es:psp,bx       ; save PSP
        mov     ax,ss
        mov     bx,es
        mov     cx,es
        sub     ax,bx
        shl     ax,4
        cli
        add     sp,ax
        ASSUME ss:DGROUP
        mov     ss,cx
        sti
        cld                                     ; only ever use post inc addressing

        mov     es:qxl_code,sp          ; ... base of code

        call    qxl_cmd                 ; process command line (sets DS)

        ASSUME ds:DGROUP

        mov     si,qxl_code                     ; ... base of code
        add     si,smsq_qxl_drl                 ; base of source string
        mov     di, OFFSET drf_drl              ; base of destination string
        mov     cx,7

        rep movsw

        mov     bl,loadf
        test    bl,bl                           ; load?
        jz      qst_shrink                      ; ... no
        call    qxl_bload                       ; bootloader

qst_shrink:
        mov     ax, OFFSET qxl_datatop  ; top of unninitialised data
        mov     bx,ds                           ; segment
        shr     ax,4                            ; top of data in paras
        inc     ax                              ; spare
        inc     ax                              ; spare
        add     bx,ax                           ; para address of top of data
        mov     ax,psp                  ; base of PSP
        sub     bx,ax                           ; size of prog

        push    es
        mov     es,ax
        mov     ah,04ah                 ; set size
        int     21h
        pop     es

        mov     bl,loadf
        test    bl,bl                           ; load?
        jnz     qst_ldupdt                      ; ... yes

qst_rstrt:
        mov     ax, OFFSET mes_rstrt            ; QXL restart message
        mov     cx, 8/2
        call    qxl_smess                       ; send this message
        jc      qst_rstrt
        mov     kbd_llck,-1                     ; force led update at restart
        mov     flowqx_kbd,-1                   ; at one go
        jmp     qst_main

qst_ldupdt:
        mov     kbd_lock, kbd_num
        call    qld_update_led

qst_main:

        cli
        call    qxl_setio                       ; set up the io drivers / interrupt servers
        call    qxl_timer_setup         ; set up the timer server
        sti
;qx2_loop:
;inc    qxl_tick                        ; count ticks
;jnz    qx2_test
;inc    qxl_tick
;call   qxl_comm
;call   qxl_rxmess
;qx2_test:
;test   kbd_stop,0ffh
;jz     qx2_loop
;jmp ex_done

        call    qxl_main

; all done

ex_done:
        cli
        call    qxl_timer_restore
        call    qxl_restore
        sti
ex_exit:
        mov     ah,4ch                          ; End Process
        int     21h

; this is an error exit for any routine that decides that the QXL has gone away

err_nresp:
        xor     ax,ax
        push    ax
        mov     ax, DGROUP
        mov     ds,ax
        mov     es,ax

        push    ds
        mov     ax, OFFSET no_reply
        push    ax

        push    ds
        mov     bx, OFFSET wrk_buff
        push    bx

        push    ds
        mov     ax, OFFSET the_QXL
        push    ax

        mov     ax,qxl
        mov     al,ah
        call    dbw_btohex                  ; convert QXL address to hex
        mov     wrk_buff,dl
        mov     wrk_buff+1,al
        mov     ax,qxl
        call    dbw_btohex
        mov     wrk_buff+2,dl
        mov     wrk_buff+3,al
        mov     wrk_buff+4,"h"
        mov     wrk_buff+5,0

; this is a simple error exit which sends a message to the console
; any routine can jump directly here with the pointer to the messages
; on the stack (last pointer = 0)

err_exit_rest:
        cli
        call    qxl_timer_restore
        call    qxl_restore
        sti

err_exit_norest:

erx_sloop:
        mov     ax, DGROUP
        mov     ds,ax
        pop     si                              ; bit of message
        test    si,si
        jz      ex_exit
        pop     ds
erx_bloop:
        lodsb
        test    al,al
        jz      erx_sloop
        push    si
        push    ds
        mov     dl,al
        mov     ah,02h                  ; send char
        int     21h
        pop     ds
        pop     si
        jmp     erx_bloop

        INCLUDE QXL_CMD.ASM
        INCLUDE QXL_BLOD.ASM
        INCLUDE QXL_STIO.ASM
        INCLUDE QXL_TIMR.ASM
        INCLUDE QXL_PCST.ASM
        INCLUDE QXL_MAIN.ASM
        INCLUDE QXL_REST.ASM
        INCLUDE QXL_COMM.ASM
        INCLUDE QXL_RXMS.ASM
        INCLUDE QXL_RTC.ASM
        INCLUDE QXL_MSE.ASM
        INCLUDE QXL_FLOW.ASM
        INCLUDE QXL_PORT.ASM
        INCLUDE QXL_LPTP.ASM
        INCLUDE QXL_COMP.ASM
        INCLUDE QXL_PHYS.ASM
        INCLUDE QXL_VMOD.ASM
        INCLUDE QXL_SND.ASM
        INCLUDE QXL_KBD.ASM
        INCLUDE QXL_LED.ASM
        INCLUDE QXL_BUFF.ASM
        INCLUDE QXL_SMES.ASM
        INCLUDE QXL_DEB.ASM

_text   ENDS

        INCLUDE QXL_DATA.ASM

stack   SEGMENT PARA STACK 'STACK'
        BYTE     256  DUP ('STACK   ')
stack   ENDS

        INCLUDE QXL_BSS.ASM

        END start
