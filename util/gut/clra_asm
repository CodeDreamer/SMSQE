; General IO utility: clear all of window   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_clra

        xref    gu_iowp

        include 'dev8_keys_qdos_io'

;+++
; This routine clears all of window
;
;       a0 c  p channel ID
;       error returns standard
;---
gu_clra
        moveq   #iow.clra,d0             ; clear all
        bra.s   gu_iowp  
        end
