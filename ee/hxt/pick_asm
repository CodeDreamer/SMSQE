; Procedure to set up PICK hotkeys  V2.00     1990   Tony Tebby

        section hotkey

        xdef    hxt_pick

        xref    hxt_res

        xref    hxt_prks
        xref    hxt_repl
        xref    hxt_mkit
        xref    hxt_adit

        xref    gu_hkuse
        xref    gu_hkfre

        include 'dev8_ee_hk_data'
        include 'dev8_keys_thg'
        include 'dev8_mac_thg'

;+++
; HOT_PICK key, name
;---
hxt_pick thg_extn PICK,hxt_res,hxt_prks
hpk.reg reg     d1/d6/a0/a1/a3
        movem.l hpk.reg,-(sp)
        moveq   #hki.pick,d6

        jsr     hxt_repl                 ; call to replace
        bne.s   hpk_exit

        move.l  8(a1),a3                 ; name
        move.w  (a3)+,d1                 ; length

        jsr     hxt_mkit                 ; allocate and make item
        bne.s   hpk_exit

        bra.s   hpk_chre

hpk_chrl
        move.b  (a3)+,(a2)+              ; characters
hpk_chre
        dbra    d1,hpk_chrl

        move.l  (a1),d1
        jsr     hxt_adit                 ; add item

hpk_exit
        movem.l (sp)+,hpk.reg
        rts

        end
