; Procedure to do a HOTKEY operation    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_do

        xref    hot_thus
        xref    hot_thfr
        xref    hot_fitem
        xref    hk_do
        xref    ut_gxnm1

;+++ 
; Do a hotkey operation
;
; HOT_DO key or name
;---
hot_do
        jsr     ut_gxnm1                 ; get one string
        bne.s   hd_exit
        jsr     hot_thus                 ; find hotkey
        bne.s   hd_exit
        jsr     hot_fitem                ; find item
        bne.s   hd_exfr
        jsr     hk_do                    ; do item
hd_exfr
        jmp     hot_thfr                 ; free hotkey
hd_exit
        rts
        end
