; HOTKEY execute Thing  V1.00   pjwitte 2oo3

; 2003-01-18    1.00            Returns job ID
;                               Based on xthg 1988 Tony Tebby  QJUMP V2.00


        section hotkey

        xdef    hk_xthid

        xref    hk_xname
        xref    gu_thexn

        include 'dev8_ee_hk_data'

;+++
; This routine executes a thing defined by a hotkey item
;
;       a1 c  p pointer to item
;       a3 c  p pointer to Hotkey linkage
;       d1 r    job ID
;
;       status return standard
;---
hk_xthid
reglist reg     d2/a0/a1/a2
        movem.l reglist,-(sp)
        jsr     hk_xname                         ; expand executable item name

        moveq   #0,d1                            ; independent
        moveq   #32,d2                           ; standard priority
        swap    d2
        jsr     gu_thexn                         ; execute it

        movem.l (sp)+,reglist
        rts
*
        end
