; General IO utility: send byte  V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_sbyte

        xref    gu_iowp

        include 'dev8_keys_qdos_io'

;+++
; This routine does simple send byte
;
;       d1 c  p byte to send
;       a0 c  p channel ID
;       error returns standard
;---
gu_sbyte
        moveq   #iob.sbyt,d0             ; send byte
; fall through into gu_iowp
        end
