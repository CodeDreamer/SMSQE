; Get single values into date

        section utility

        xdef    ut_gsdat
;+++
; This routine converts single values of the parts of a date into a
; standard QDOS date longword.
;
;               Entry                   Exit
;       d0.w    year (start at 1980)    smashed
;       d1.b    month                   date (long)
;       d2.b    day                     smashed
;       d3.b    hour                    smashed
;       d4.b    minute                  smashed
;       d5.b    second                  smashed
;---
dat_reg reg     d6-d7/a0
ut_gsdat
        movem.l dat_reg,-(sp)
        sub.w   #1980,d0
        move.w  d0,-(sp)        ; preserve year
        move.l  #$23bc1880,d7   ; thats 01/01/1980, 00:00:00
clock_yr
        tst.b   d0              ; year ok?
        beq.s   clock_lp        ; yes, continue with leap-year
        add.l   #$1e13380,d7    ; exactly one year
        subq.b  #1,d0
        bra.s   clock_yr
clock_lp
        move.w  (sp),d0         ; check for leap-year
        and.w   #$fffc,d0       ; by removing non-multiples of 4
        cmp.w   (sp)+,d0        ; is it a leap-year?
        seq     d6              ; if so, set flag
        lsr.b   #2,d0           ; number of leap-years
clock_la
        tst.b   d0
        beq.s   clock_mn
        add.l   #$15180,d7      ; add one day for every leap-year
        subq.b  #1,d0
        bra.s   clock_la
clock_mn
        tst.b   d6              ; current year leap-year?
        beq.s   clock_nl        ; no!
        cmp.b   #$3,d1          ; january or february?
        bge.s   clock_nl        ; yes
        sub.l   #$15180,d7      ; subtract one day
clock_nl
        lea     months(pc),a0
        moveq   #0,d6           ; count for number of days ..
clock_nd
        subq.b  #1,d1           ; .. except current month
        beq.s   clock_cm
        add.w   (a0)+,d6        ; add days of month
        bra.s   clock_nd
clock_cm
        ext.w   d2              ; make day a word
        add.w   d2,d6           ; total number of days
        addq.w  #1,d6           ; plus current
clock_da
        subq.w  #1,d6
        beq.s   clock_se
        add.l   #$15180,d7      ; add days
        bra.s   clock_da
clock_se
        ext.w   d5
        ext.l   d5              ; make seconds long ...
        add.l   d5,d7           ; ... and add

        ext.w   d4              ; make minutes word,
        mulu    #60,d4          ; minutes .. now in seconds
        add.l   d4,d7
        ext.w   d3              ; make hours word ...
        mulu    #3600,d3        ; hours .. in seconds
        add.l   d3,d7

        move.l  d7,d1
        movem.l (sp)+,dat_reg
        moveq   #0,d0
        rts
 
months  dc.w    31,28,31,30,31,30,31,31,30,31,30,31
        rts

        end
