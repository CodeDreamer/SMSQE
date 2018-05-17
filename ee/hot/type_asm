; Function to return type of HOTKEY  V2.00    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_type

        xref    hot_thus
        xref    hot_thfr
        xref    hot_fitem
        xref    hot_rter
        xref    ut_gxnm1
                         
        include 'dev8_ee_hk_data'

;+++ 
; Return type of Hotkey
;
; value = HOT_TYPE (key or name)
;---
hot_type
        jsr     ut_gxnm1                 ; get one string
        bne.s   ht_exit
        jsr     hot_thus                 ; find hotkey
        bne.s   ht_exit

        jsr     hot_fitem                ; find item
        bne.s   ht_rter
        moveq   #$fffffffe,d0            ; ignore transient flag
        move.w  hki_type(a1),d0          ; find type
        ext.l   d0
ht_rter
        jsr     hot_rter                 ; return value

ht_exfr
        jmp     hot_thfr                 ; free hotkey
ht_exit
        rts
        end
