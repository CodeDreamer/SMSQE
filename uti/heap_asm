; Common heap management

        section utility

        xdef    ut_alchp
        xdef    ut_rechp

        include dev8_keys_qdos_sms
;+++
; Allocate memory from common heap for current job.
;
;               Entry                           Exit
;       d1.l    nr of bytes required            nr of bytes allocated
;       d2+                                     all preserved
;       a0                                      base address of area
;       a1+                                     all preserved
;
;       D0 and condition codes set on return
;---
ut_alchp
        movem.l d2-d3/a1-a3,-(sp)
        moveq   #sms.achp,d0
        moveq   #-1,d2
        trap    #do.sms2
        movem.l (sp)+,d2-d3/a1-a3
        tst.l   d0
        rts

;+++
; Release memory to common heap.
;
;               Entry                           Exit
;       a0      pointer to heap entry           preserved
;       a1+                                     all preserved
;
;       This routine cannot fail!
;---
ut_rechp
        movem.l d0-d3/a0-a3,-(sp)
        moveq   #sms.rchp,d0
        trap    #do.sms2
        movem.l (sp)+,d0-d3/a0-a3
        tst.l   d0
        rts

        end

        end
