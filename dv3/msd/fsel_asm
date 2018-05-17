; DV3 MSDOS Format Select            V3.00           1993 Tony Tebby

        section dv3

        xdef    msd_fsel

        include 'dev8_dv3_keys'
        include 'dev8_keys_err'

;+++
; DV3 MSDOS Format Select
;
;       d0 cr   format type / error code
;       d7 c  p drive ID / number
;       a0 c  p pointer to physical format table
;       a3 c  p pointer to linkage
;       a4 c  p pointer to drive definition
;
;       status return standard
;
;---
msd_fsel
        moveq   #err.nimp,d0
        rts

        end
