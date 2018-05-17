; General IO utility: fetch multiple bytes   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_fmul
        xdef    gu_flin

        xref    gu_iow

        include 'dev8_keys_qdos_io'

;+++
; This routine does simple fetch line, using d1, preserving a1
;
;       d1 cr   buffer size / length of line
;       a0 c  p channel ID
;       a1 c  p pointer to buffer
;       error returns standard
;---
gu_flin
        moveq   #iob.flin,d0             ; fetch line
        bra.s   guf_do
;+++
; This routine does simple fetch multiple bytes, using d1, preserving a1
;
;       d1 cr   number of bytes to fetch / number fetched
;       a0 c  p channel ID
;       a1 c  p pointer to buffer
;       error returns standard
;---
gu_fmul
        moveq   #iob.fmul,d0             ; fetch multiple bytes
guf_do
        movem.l d2/a1,-(sp)
        move.w  d1,d2
        bsr.s   gu_iow
        movem.l (sp)+,d2/a1
        rts
        end
