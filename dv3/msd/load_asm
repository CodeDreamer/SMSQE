; DV3 MSDOS Scatter Load              V3.00           1993 Tony Tebby

        section dv3

        xdef    msd_ld3
        xdef    msd_ld4


        include 'dev8_keys_hdr'
        include 'dev8_dv3_keys'
        include 'dev8_keys_err'

;+++
; DV3 MSDOS Scatter Load
;
;       d1 c  u 0 on first entry
;       d2 c s  length of file
;       d3 c s  0 on first entry
;       d6 c  p file ID
;       d7 c  p drive ID / number
;       a0 c  p pointer to channel block
;       a1 c  p pointer to file image
;       a3 c  p pointer to linkage
;       a4 c  p pointer to drive definition
;
;       status return standard
;
;---
msd_ld3
msd_ld4
        moveq   #err.nimp,d0
        rts

        end
