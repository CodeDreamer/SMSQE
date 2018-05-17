; Clear workspace of current job

        section utility

        xdef    ut_clwsp
;+++
; Clear workspace of current job and set main pointer to it.
;
;               Entry                           Exit
;       a4      base of data area rel (a6)      smashed
;       a6      base of job                     absolute pointer to data area
;---
ut_clwsp
        lea     (a6,a4.l),a6            ; main pointer
        move.l  a6,a4                   ; clear workspace ...
clr_wspc
        clr.w   (a4)+
        cmp.l   a4,a7                   ; job's stack reached?
        bgt.s   clr_wspc
        rts

        end
