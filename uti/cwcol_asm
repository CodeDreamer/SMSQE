; Calculate Colourway colours for console

        section utility

white   equ     7
green   equ     4
red     equ     2
black   equ     0

        xdef    ut_cwcol

;+++
; Calculate Colourway colours for console.
;
;               Entry                           Exit
;       D2.b    colourway (0-3)          D2.l   border,width,paper,ink
;       D3      border width
;---
ut_cwcol
        and.w   #$f,d2
        lsl.b   #2,d2
        move.l  norm_tab(pc,d2.w),d2
        add.b   d3,d2
        swap    d2
        rts

norm_tab
;               paper,ink,border,width

        dc.b    white,black,green,0
        dc.b    black,white,red,0
        dc.b    white,black,red,0
        dc.b    black,white,green,0

        end
