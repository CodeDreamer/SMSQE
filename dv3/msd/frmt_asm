; DV3 MSDOS Format             V3.00           1993 Tony Tebby

        section dv3

        xdef    msd_frmt

        include 'dev8_dv3_keys'
        include 'dev8_keys_err'

;+++
; DV3 MSDOS Format
;
;       d0 cr   format type / error code
;       d7 c  p drive ID / number
;       a1 c  p pointer to name
;       a3 c  p pointer to linkage
;       a4 c  p pointer to drive definition
;
;       status return standard
;
;---
msd_frmt
        moveq   #err.nimp,d0
        rts

        end
