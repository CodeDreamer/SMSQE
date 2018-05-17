; General IO utility: do IO, wait, preserve regs  V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_iowp

        include 'dev8_keys_qdos_io'

        nop              ; prevent bra.s failing
;+++
; This routine does simple io (wait forever) preserving regs
;
;       d0 cr   action / error code
;       d1 c  p call parm
;       a0 c  p channel ID
;       a1 c  p call parm
;       error returns standard
;---
gu_iowp
        movem.l d1/d3/a1,-(sp)
        moveq   #forever,d3              ; wait forever
        trap    #do.io
        movem.l (sp)+,d1/d3/a1
        tst.l   d0
        rts
        end
