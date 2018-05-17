        section gw
* change octants
        xdef    GW_CHOCT

        include 'dev8_Minerva_INC_GW'

* D6 -i o- A
* D7 -i o- B
* A0 -i o- D
* A1 -i o- K1
* A2 -i o- K2
* A3 -i o- K3
* A6 -ip - stack frame

* D4-D5 destroyed

gw_choct
        not.b   OCTCH(A6)       which one are we expecting?
        bne.s   DIAG            if diagonal, go do it

* Do a square octant change, to get B positive

        tst.w   SDX(A6)
        bne.s   DDXOK
        neg.w   DDX(A6)
ddxok

* If SDY=0:DDY=-DDY
        tst.w   SDY(A6)
        bne.s   DDYOK
        neg.w   DDY(A6)
ddyok

* Calculate new variable values for D,B,A,K1,K2,K3

        move.l  A1,D4
        neg.l   D4
        move.l  D4,A1           K1' = -K1
        add.l   A2,D4           W = K2 + K1'
        move.l  D4,A2
        add.l   A1,A2           K2' = W + K1'
        neg.l   D7
        add.l   D4,D7           B' = W - B
        move.l  D7,D5
        sub.l   D6,D5
        sub.l   A0,D5
        move.l  D5,A0           D' = B' - A - D
        sub.l   D7,D6
        sub.l   D7,D6
        add.l   D4,D6           A' = A - 2*B' + W
        lsl.l   #2,D4
        sub.l   A3,D4
        move.l  D4,A3           K3' = 4*W - K3

        rts

* Change the movement data across a diagonal boundary

diag
        tst.w   SDY(A6)
        bne.s   SDYOK           if SDY is zero
        clr.w   SDX(A6)         SDX = 0
        move.w  DDY(A6),SDY(A6) SDY = DDY
        bra.s   SDXOK

sdyok
        tst.w   SDX(A6)
        bne.s   SDXOK           else if SDX is zero
        clr.w   SDY(A6)         SDY = 0
        move.w  DDX(A6),SDX(A6) SDX = DDX
sdxok

* Calculate new D,B,A,K1,K2 and K3

        move.l  A3,D4
        neg.l   D4
        move.l  D4,A3           K3' = -K3
        add.l   A2,D4
        add.l   A2,D4           W = 2*K2 + K3'
        add.l   A3,A2           K2' = K2 + K3'
        move.l  D4,D5
        sub.l   A1,D5
        move.l  D5,A1           K1' = W - K1
        move.l  A2,D5
        asr.l   #1,D5
        add.l   D6,D5
        add.l   D5,D7           B' = B + A + K2'/2
        asr.l   #1,D5
        add.l   A0,D5
        neg.l   D5
        add.l   D7,D5
        move.l  D5,A0           D' = B' - D - A/2 - K2'/4
        asr.l   #1,D4
        add.l   D4,D6
        neg.l   D6              A' = - W/2 - A

        rts

        end
