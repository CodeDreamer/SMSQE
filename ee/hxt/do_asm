; Procedure to do a HOTKEY operation    1990   Tony Tebby

        section hotkey

        xdef    hxt_do

        xref    hxt_dost

        xref    thp_str

        xref    hk_fitem
        xref    hk_do
        xref    gu_hkuse
        xref    gu_hkfre

        include 'dev8_ee_hk_data'
        include 'dev8_keys_thg'
        include 'dev8_mac_thg'

;+++ 
; Do a Hotkey operation
;
; HOT_DO key or name
;---
hxt_do thg_extn {DO  },hxt_dost,thp_str
hdo.reg reg     d1/d2/a1/a3/a6
        movem.l hdo.reg,-(sp)

        move.l  4(a1),a1                 ; real pointer to string

        jsr     gu_hkuse                 ; use hotkey
        bne.s   hdo_exit

        jsr     hk_fitem                 ; find it
        bne.s   hdo_free
        lea     -$80(sp),a6              ; top of workspace for hk_pick
        jsr     hk_do                    ; do item
hdo_free
        jsr     gu_hkfre                 ; free the hotkey system

hdo_exit
        movem.l (sp)+,hdo.reg
        rts
        end
        bne.s   hd_exit
        jsr     hxt_thus                 ; find hotkey
        bne.s   hd_exit
        jsr     hxt_fitem                ; find item
        bne.s   hd_exfr
        jsr     hk_do                    ; do item
hd_exfr
        jmp     hxt_thfr                 ; free hotkey
hd_exit
        rts
        end
