        section gw
* transform conic section coefficients
        xdef    GW_TRANS

        xref    GW_PIXAD,GW_CHOCT

        include 'dev8_Minerva_INC_GW'
        include 'dev8_Minerva_INC_assert'

* The equation which we have been told to work with is:

* F(x,y) = v * x - u * y + alpha/2 * y^2 + beta/2 * x^2 - gamma * x*y

* We are to draw F(x,y) = 0. We are to set off towards x=x0+u, y=y0+v, with
* the second order terms.
* If either alpha or beta is zero, both should be (our forte is ellipses, not
* paraboli).
* Finally, we should have gamma^2 <= alpha * beta, (as we're not too hot on
* drawing hyperboli!) with equality only when they are all zero (not too good
* at drawing pairs of intersecting, or parallel, lines!). Unfortuanately, it's
* all too late now to check this without great hassle, so it's possible we
* may draw just half of one side of a very narrow ellipse.

* Octants:                            V
*                                     ^
*             Y             *-*-*- -+- -*- -+- -*   Choice of initial octant
*             ^             | 3 * 2 | 2 * 1 | 1 *   such that anticlockwise
*       \  2  |  1  /       +- -*-*-*- -*- -*-*-*   rotation works properly
*         \   |   /         | 3 | 3 * 2 * 1 * 0 |   across each boundary
*       3   \ | /   0       *-*-*-*-*-*-*-*-*- -+
*       ------O------>X     | 4 | 4 * O * 0 | 0 |-->U
*       4   / | \   7       +- -*-*-*-*-*-*-*-*-*
*         /   |   \         | 4 * 5 * 6 * 7 | 7 |   What happens at u=0, v=0
*       /  5  |  6  \       *-*-*- -*- -*-*-*- -+   is pretty irrelevent!
*                           * 5 | 5 * 6 | 6 * 7 |
*                           *- -+- -*- -+- -*-*-*

* D0 -ip - start point x coordinate (lsw)
* D1 -ip - start point y coordinate (lsw)
* D2 -i o- alpha / pixel word mask
* D3 -i o- beta / colour masks
* D4 -i  - gamma
* D6 -i o- u / a0
* D7 -i o- v / b0
* A0 -i o- channel definition / d0
* A1 -  o- k1
* A2 -  o- k2
* A3 -  o- k3
* A5 -  o- pixel word address
* A6 -ip - stack frame

* D5 destroyed

gw_trans

* Clean up which way we're being told to curve. If it's not anticlockwise,
* we effectively mirror across the Y-axis.

        clr.w   -(SP)           set square y increment in lsw to zero
        move.w  XINC(A6),-(SP)  get basic x increment into msw
        move.l  D2,D5
        or.l    D3,D5           are alpha and beta non-negative?
        bpl.s   CLOCK           yes - going anticlockwise already
        neg.l   D2              negate alpha
        neg.l   D3              negate beta
        neg.l   D6              negate u
        neg.w   (SP)            negate dx
clock

* Next we can simplify our initial octant decisions by a 180 degree rotation

        moveq   #1,D5           diagonal y-increment
        tst.l   D6
        bpl.s   ROTOK           if u > 0, leave it alone
        bne.s   ROTGO           if u < 0, rotate
        tst.l   D7
        ble.s   ROTOK           if u = 0 and u <= 0, leave it alone
rotgo
        neg.l   D6              negate u
        neg.l   D7              negate v
        neg.w   D5              change y increment to minus one
        neg.w   (SP)            negate dx
rotok

        move.w  (SP),DDX(A6)    store octant zero diagonal move dx
        move.w  D5,DDY(A6)      store octant zero diagonal move dy
        assert  SDX,SDY-2
        move.l  (SP)+,SDX(A6)   store octant zero square move dx,dy

* Now set up for how many octant changes remain to be done
* We will only need to do at most one clockwise or one or two anti-clockwise
* The reason this has had to be very finicky is because we must ensure that
* the octant we start in always curls clockwise properly.

        move.l  D7,-(SP)
        smi     OCTCH(A6)       if v >= 0, clockwise is diagonal first
        bmi.s   OCT67           if v < 0, we need at least one anticlockwise
        not.l   (SP)            if v >= 0, we may need one clockwise
oct67
        add.l   D6,(SP)         if 0 <= u < -v or 0 < u <= v ... see later

* Now set up all the algorithm's registers

        move.l  D3,A1           k1 = beta
        sub.l   D4,D3
        move.l  D3,A2           k2 = beta - gamma
        move.l  D2,A3
        sub.l   D4,A3
        add.l   A2,A3           k3 = alpha - 2*gamma + beta
        asr.l   #1,D3
        add.l   D3,D7           b0 = v + k2/2
        asr.l   #1,D2
        sub.l   D6,D2
        asr.l   #1,D2
        add.l   D7,D2           d0 = b0 - u/2 + alpha/4
        sub.l   D7,D6           a0 = u - b0

        move.l  D2,-(SP)        save d0
        jsr     GW_PIXAD(PC)    find pixel address and masks
        move.l  (SP)+,A0        put d0 where it belongs

* Finally, we set which octant we are starting in

        move.b  OCTCH(A6),3(SP) did we select anticlockwise? (and save it)
        bsr.s   CHOCT           always one octant change if going anticlockwise
        tst.b   (SP)+           did we want one clockwise or two anticlockwise?
        bsr.s   CHOCT           one more possible octant change
        move.w  (SP)+,D5        get back which way we were spinning
        eor.b   D5,OCTCH(A6)    from here on in on we go clockwise
anrts
        rts

choct
        bpl.s   ANRTS
        jmp     GW_CHOCT(PC)    we only do this if -ve flag was set

        end
