; General IO utility: do IO with wait  V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_iow

        include 'dev8_keys_qdos_io'

        nop               ; prevent bra.s failing
;+++
; This routine does simple io (wait forever)
;
;       d0 cr   action / error code
;       d1 cr   call parm / return from call
;       a0 c  p channel ID
;       a1 cr   call parm / return from call
;       error returns standard
;---
gu_iow
        move.l  d3,-(sp)
        moveq   #forever,d3              ; wait forever
        trap    #do.io
        move.l  (sp)+,d3
        tst.l   d0
        rts
        end
