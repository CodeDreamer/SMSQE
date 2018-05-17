; Vector Release memory to common heap

        section utility

        xdef    ut_sprhp

        include dev8_keys_qlv
;+++
; Vector Release memory to common heap.
; Note that the area pointed to is not the usable area, it is the heap header!
; This routine must be called in Supervisor mode!
;
;               Entry                           Exit
;       a0      pointer to heap entry           preserved
;       a1+                                     all preserved
;
;       This routine cannot fail!
;---
ut_sprhp
        movem.l d1-d3/a0-a3,-(sp)
        move.w  mem.rchp,a2
        jsr     (a2)
        movem.l (sp)+,d1-d3/a0-a3
        rts

        end
