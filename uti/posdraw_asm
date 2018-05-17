; Position and draw window at given origin      1991,93 Jochen Merz V0.01

        include dev8_keys_wwork
        include dev8_keys_wman

        section program

        xdef    ut_posdw        ; position window and draw it
        xdef    ut_posnod       ; position window without drawing

        xref    ut_gworg

;+++
; Calculate the right origin, position window and draw it completely.
;
;               Entry                   Exit
;       D1.l    origin or -1
;               ...
;       Condition codes set on return.
;---
ut_posdw
        bsr.s   ut_posnod               ; first position window
        bne.s   pos_rts
        jmp     wm.wdraw(a2)            ; then draw window
pos_rts
        rts
;+++
; Calculate the right origin and position window.
;
;               Entry                   Exit
;       D1.l    origin or -1
;               ...
;       Condition codes set on return.
;---
ut_posnod
        tst.l   d1
        blt.s   pos_drcr                ; draw at current position
        bsr     ut_gworg
        add.l   d2,d1                   ; add window origin
        add.l   ww_xorg(a4),d1
pos_drcr
        jmp     wm.prpos(a2)            ; position (pulldown) window

        end
