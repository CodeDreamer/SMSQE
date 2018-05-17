; General IO utility: send multiple bytes  V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_smul

        xref    gu_iowp

        include 'dev8_keys_qdos_io'

;+++
; This routine sends multiple bytes
;
;       d2 c  p number of bytes to send
;       a0 c  p channel ID
;       a1 c  p pointer to bytes
;       error returns standard
;---
gu_smul
        moveq   #iob.smul,d0
        bra.s   gu_iowp
        end

