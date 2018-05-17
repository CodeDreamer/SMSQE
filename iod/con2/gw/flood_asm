        section gw
* trap to set fill mode/vectors
        xdef    GW_FLOOD

        xref    iou_achb,iou_rchb

        include 'dev8_Minerva_INC_SD'
        include 'dev8_Minerva_INC_ERR'
        include 'dev8_Minerva_INC_assert'

fillbuf equ     4*1024  !!!!
hp_end  equ     16

* The parameter in D0.L may be one of:
*             0 - switch fill off
*             1 - switch fill on and clear buffer
*  even (not 0) - address of fill vectors
*   odd (not 1) - (e.g. -1) to clear user fill vectors

* Note that D0-D1/A0 only are touched by ALCHP, with d1 maybe increased.
* Also RECHP returns D0=0 and destroys A0.

* D0 -  o- error code
* D1 -i  - parameter
* A0 -ip - channel definition block
* A1 destroyed

gw_flood
        moveq   #0,D0           no error
        move.l  D1,-(SP)
        lea     SD_FUSE(A0),A1  set convenient pointer
        lsr.w   #1,D1           check parameter
        beq.s   FLOOD           0/1, go do flood stuff
        bcc.s   VECTOR
        clr.l   (SP)            an odd number restores default fill action
vector
        move.l  (SP)+,(A1)      update the user vector
        bra.s   DONE

flood
        tst.b   SD_FMOD-SD_FUSE(A1) check current fill mode
        assert  SD_FBUF,SD_FUSE-4
        move.l  -(A1),A0        pick up any old buffer
        beq.s   WASNT           skip if current fill mode was zero
        jsr     iou_rchb(PC)    if it was on, release the buffer and say so
wasnt
        move.l  (SP)+,D1
        beq.s   CLEAR           if new mode is zero then wipe it out
        move.l  #HP_END+FILLBUF,D0
        jsr     iou_achb(PC)    allocate and clear a new buffer
        beq.s   SETIT
clear
        sub.l   A0,A0           turning mode off or any error, clear buffer
setit
        move.l  A0,(A1)+        update the buffer pointer
        sne     D1
        neg.b   D1
        move.b  D1,SD_FMOD-SD_FUSE(A1) finally, set fill mode zero/one
done
        lea     -SD_FUSE(A1),A0 put back channel definition block pointer
        rts

        end
