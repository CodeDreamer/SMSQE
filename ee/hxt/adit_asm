; Add item      V2.00     1990   Tony Tebby   QJUMP

        section exten

        xdef    hxt_adit

        xref    hk_newst

        xref    gu_hkuse
        xref    gu_hkfre

        include 'dev8_ee_hk_data'

;+++
; Add item
;
;       d1 cs   HOTKEY (word)
;       a0 c  p pointer to item
;       a1  s
;       a3  s
;       status return 0 or err.fdiu
;---
hxt_adit
        move.l  a0,a1
        jsr     gu_hkuse                 ; use hotkey
        bne.s   hai_exit

        jsr     hk_newst                 ; add new one
        jsr     gu_hkfre                 ; free the hotkey system

hai_exit
        rts
        end
