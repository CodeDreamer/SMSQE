; Calculate Colourway colours           1989 Jochen Merz

        section utility

white   equ     7
green   equ     4
red     equ     2
black   equ     0

        xdef    ut_cccol

;+++
; Calculate Colourway colours.
;
;               Entry                   Exit
;       D2.b    colourway (0-3)         normal paper.w / ink.w
;       D3                              highlight paper.w / ink.w
;---
ut_cccol
        moveq   #1,d3
        and.b   d2,d3
        lsl.b   #2,d3
        move.l  norm_tab(pc,d3.w),d2
        move.l  high_tab(pc,d3.w),d3
        rts

norm_tab
        dc.w    white,black
        dc.w    black,white
high_tab
        dc.w    green,black
        dc.w    red,white

        end
