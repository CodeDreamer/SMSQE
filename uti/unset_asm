; Unset a window definition

        section utility

        xdef    ut_unset
;+++
; Unset a window definition and release its space
;
;               Entry                           Exit
;       a4      window working definition
;---
ut_unset

mt.alchp equ    $18
mt.rechp equ    $19
wm.unset equ    $14
wm.setup equ    $04

         movem.l  d0/a0,-(sp)          keep channel ID
         jsr      wm.unset(a2)      unset current definition
         move.l   a4,a0
archreg  reg      d1-d3/a1-a3
         moveq    #mt.rechp,d0
         movem.l  archreg,-(sp)
         moveq    #-1,d2
         trap     #1
         movem.l  (sp)+,archreg
         movem.l  (sp)+,d0/a0
         rts

        end
