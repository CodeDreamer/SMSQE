; QXL_LED.ASM Keyboard LED update
; 2006.10.01	1.00	created by BC


qxl_do_led:
        push    ax
        mov     al,flowqx_kbd
        cmp     al,kbd_llck                     ; led update needed?
        jz      qld_no_led
        call    qld_from_qxl
        mov     kbd_llck,al                     ; save current lock

qld_no_led:
        pop     ax
        retn

qld_from_qxl:
        push    ax
        xor     ax,ax
        mov     ah,flowqx_kbd
        and     ah,1
        shl     ah,2
        or      al,ah
        mov     ah,flowqx_kbd
        and     ah,2
        shr     ah,1
        or      al,ah
        or      al,kbd_num                      ; Num lock always on
        mov     kbd_lock,al
        call    qld_update_led
        pop     ax
        retn

qld_from_bios:
        push    ax
        push    es
        xor     ax,ax
        mov     es,ax
        ASSUME es:BASESEG
        mov     al,es:kbd_byt2
        and     al,kbd_mask
        mov     kbd_lock,al
        pop     es
        ASSUME es:DGROUP
        call    qld_update_led
        pop     ax
        retn

qld_update_led:
        mov     ah,kbd_disa
        call    qld_write_prog
        mov     ah,kbd_ldup
        call    qld_write_data
        mov     ah,kbd_lock
        call    qld_write_data
        mov     ah,kbd_enab

qld_write_prog:
        call    qld_wait_write
        mov     al,ah
        out     kbd_prog,al
        retn

qld_write_data:
        call    qld_wait_write
        mov     al,ah
        out     kbd_data,al
        retn

qld_wait_write:
        in      al,kbd_prog
        test    al,2
        jnz     qld_wait_write
        retn

qld_wait_read:
        in      al,kbd_prog
        test    al,1
        jz      qld_wait_read
        retn
