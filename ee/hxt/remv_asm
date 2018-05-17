; Hotkey procedure to remove an item      V2.00     1990   Tony Tebby   QJUMP

        section exten

        xdef    hxt_remv

        xref    hxt_do

        xref    thp_str

        xref    hk_remv

        xref    gu_hkuse
        xref    gu_hkfre

        include 'dev8_keys_thg'
        include 'dev8_ee_hk_data'
        include 'dev8_mac_thg'

;+++
; HOT_REMV key / name
;---
hxt_remv thg_extn {REMV},hxt_do,thp_str

hrm.reg reg     a1/a3
        movem.l hrm.reg,-(sp)

        move.l  4(a1),a1                 ; name / key
        jsr     gu_hkuse                 ; use hotkey
        bne.s   hrm_exit

        jsr     hk_remv                  ; remove hotkey

        jsr     gu_hkfre

hrm_exit
        movem.l (sp)+,hrm.reg
        rts
        end
