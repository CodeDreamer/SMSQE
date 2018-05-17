        section gw
* Pitteway's algorithm for conic sections
        xdef    GW_PITT

        xref    GW_PIXEL,GW_CHOCT

        include 'dev8_Minerva_INC_GW'
        include 'dev8_Minerva_INC_SV'
        include 'dev8_Minerva_INC_assert'

* The original algorithm was based on the function value at the mid-point
* of the line between the square or diagonal move's finish points.
* I have changed this to be the average of the function values at those two
* points, which ensures that the algorithm tracks its way along using points
* whose functional values are the closest to zero of those available.
* The original algorithm failed to do this, and as a result was liable to go
* astray when presented with nearly equal values, as the mid point value was
* not giving the correct idea of which point was nearest the curve. This would
* become somewhat unpredictable for small ellipses, whereas my method is at
* least predictable. LWR.

* The algorithm now goes like this:

* 1) If the start and end points are the same, lines and arcs of less than five
*       octants stop without drawing anything.

* 2) Draw the first point.

* 4) If the octant count is greater than four, draw the next point, unless it
*       repeats the start or actually is the finish. This helps with small arcs
*       and ellipses.

* 5) Thereafter, when we get the octant count down less than three, if we're
*       about to draw a point adjacent to the finish point, do so and stop in
*       all cases except degenerate ellipses of more than one pixel.

* This is still not perfect, as silly little arcs may overshoot by one pixel.

* Also, this now checks for octant changing at the right time, i.e. just when
* "d" is about to be updated. If the selected move takes "d" further away from
* zero, THAT's the time to do an octant change (if there are any left!).

* D0 -i  - x coordinate (lsw)
* D1 -i  - y coordinate (lsw)
* D2 -ip - pixel word mask (lsw)
* D3 -i  - colour masks
* D6 -i  - a
* D7 -i  - b
* A0 -i  - d
* A1 -i  - k1
* A2 -i  - k2
* A3 -i  - k3
* A5 -i  - pixel word address
* A6 -ip - stack frame

* D4-D5 destroyed

gw_pitt
        moveq   #4,D4
        assert  X0,Y0-2
        move.l  X0(A6),D5
        assert  X1,Y1-2
        cmp.l   X1(A6),D5
        bne.s   XYOK            if start and end coincide
        addq.b  #1,TYPE(A6)     ... cancel degenerate ellipse flag ...
        bpl.s   XYOK            ... and most things carry on, but ...
        cmp.b   OCTS(A6),D4     ... an arc of less than 5 octants ...
        ble.s   XYOK            ... or a line stops here
        rts

squbad
        tst.b   OCTCH(A6)       are we expecting a square change?
        beq.s   SQUGO           no - leave well enough alone
        sub.l   A1,D7           put b back as it was
        bra.s   DOCH            go try next octant

diabad
        tst.b   OCTCH(A6)       are we expecting a diagonal change?
        bne.s   DIAGO           no - leave well enough alone
        add.l   A3,D6           put a back as it was
doch
        jsr     GW_CHOCT(PC)    go change octant
        subq.b  #1,OCTS(A6)     have we actually used up all our octants?
        bge.s   WHATMOVE        no - try out our new position
        addq.b  #3,OCTS(A6)     just maybe...
        bclr    #4,TYPE(A6)     is it an arc, and is this our first time here?
        bne.s   WHATMOVE        yes - we'll let it carry on looking for end pt.
        bra     ALLOVER         no - then pack it in now

xyok
        move.w  #$7FFC,-(SP)    set up our counter for the second point
        cmp.b   OCTS(A6),D4     have we got more than four octants going?
        bgt.s   NOSPEC
        addq.w  #8,(SP)         yes - give it a chance to get away
nospec

* execution routes here are optimised for line drawing

* Plot a point and move on to next
mainloop
        jsr     GW_PIXEL(PC)    plot the point

whatmove
        move.l  A0,D4           which move is best?
        bge.s   DIAMOV

* Square move

        add.l   A1,D7           b += k1
        bmi.s   SQUBAD          ahah! this won't help, go see what to do!
squgo
        sub.l   A2,D6           a -= k2
        add.l   D7,A0           d += b
        assert  SDX,SDY-2
        move.l  SDX(A6),D4      square move dx and dy
        bra.s   DOY

* Diagonal move

diamov
        sub.l   A3,D6           a -= k3
        bmi.s   DIABAD          ahah! this won't help, go see what to do!
diago
        add.l   A2,D7           b += k2
        sub.l   D6,A0           d -= a
        assert  DDX,DDY-2
        move.l  DDX(A6),D4      diagonal move dx and dy

* now adjust the screen address, and masks

doy
        tst.w   D4
        beq.s   DOX
        add.w   D4,D1           y += dy
        add.w   LINEM+1(A6,D4.W),A5 move scr ptr by a line (words -LINEL,LINEL)
        swap    D3              swap colour mask

dox
        swap    D4
        tst.w   D4
        beq.s   COUNTIT
        bmi.s   XLESS
        add.w   D4,D0           x += dx
        ror.w   D4,D2           if dx > 0 rotate pixel mask right by it
        bcc.s   COUNTIT
        addq.l  #2,A5           if wrapped then move screen ptr forward a word
        bra.s   COUNTIT

xless
        add.w   D4,D0           x += dx
        neg.w   D4
        rol.w   D4,D2           if dx < 0 rotate pixel mask left by it negated
        bcc.s   COUNTIT
        subq.l  #2,A5           if wrapped then move screen ptr back a word

countit
        subq.w  #4,(SP)         count down our special flag + count
        ble.s   SPECIAL         if greater than zero, we're happy in main pass

chkend
        cmp.b   #2,OCTS(A6)     are we into the last couple of octants?
        bgt.s   MAINLOOP        ... not even on last one or two

        move.w  D1,D5           check if within one pixel in y
        sub.w   Y1(A6),D5
        addq.w  #1,D5
        subq.w  #3,D5           ... within one?
        bcc.s   MAINLOOP        nope - carry on
        move.w  D0,D4           check if within one pixel in x
        sub.w   X1(A6),D4
        bpl.s   DXISPOS
        neg.w   D4
dxispos
        cmp.w   XINC(A6),D4     within 1 for 512, or 2 for 256 mode?
        bhi.s   MAINLOOP        no - carry on
lastone
        move.w  #4,(SP)         set counter so it'll drop out after this one
                ;               but make certain this isn't the first or last!

special
        beq.s   ALLOVER         pixel count hit zero on main pass
        move.w  D0,D5
        swap    D5
        move.w  D1,D5
        cmp.l   X0(A6),D5       is this an attempt to redraw the first point?
        beq.s   KILLIT          yes - stop now
        cmp.l   X1(A6),D5       is this an attempt to draw the end point?
        bne     MAINLOOP        no - carry on
allover
;       move.w  #4,(SP)         one last thing ...
;       subq.b  #8,TYPE(A6)     ... is the degenerate ellipse flag still set?
;       bne.s   MAINLOOP        yes - then we want to draw the last point
; The above is not currently in use. If it seems a good idea, the ellipse
; calculation should change its TYPE flag from $3D to $08 when it decides that
; an ellipse is degenerate, and changes it to a line.
killit
        addq.l  #2,SP           lose counter
        rts

        end
