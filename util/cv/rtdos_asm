; Convert real time to DOS time       V0.00   1993 Tony Tebby

        section cv

        xdef    cv_rtdos

        xref    cv_mnths

;+++
; Convert real time to DOS time
;
;       d1 cr  real time / DOS time (if zero, unchanged)
;       status return 0
;---
cv_rtdos
rtd.reg reg     d2/a0

        move.l  d1,d0
        beq.s   rtd_rts

        movem.l rtd.reg,-(sp)

        lsr.l   #1,d1
        divu    #24*60*30,d1             ; split into days / 2 seconds

; first do the days

        sub.w   #19*365+4,d1             ; set 1980 base
        moveq   #0,d0
        move.w  d1,d0
        divu    #365*4+1,d0              ; four year cycle
        move.l  d0,d2
        clr.w   d2
        swap    d2                       ; four year day
        lsl.w   #2,d0                    ; years

        cmp.w   #31+28,d2                ; is it Feb 29?
        blt.s   rtd_day
        bgt.s   rtd_day1

        move.w  d0,d1                    ; real year
        moveq   #4,d0                    ; leap month * 2
        moveq   #29,d2                   ; leap day
        bra.s   rtd_pday

rtd_day1
        subq.w  #1,d2                    ; after Feb 29 adjust by one day
rtd_day
        divu    #365,d2

        add.w   d2,d0                    ; real years
        move.w  d0,d1                    ; where we need it
        swap    d2                       ; day of year

        move.w  d2,d0
        lsr.w   #5,d0
        add.w   d0,d0
        addq.w  #6,d0                    ; first guess at month

        lea     cv_mnths-2(pc),a0
        add.w   d0,a0
rtd_mloop
        subq.w  #2,d0
        cmp.w   -(a0),d2                 ; start less than month?
        blt.s   rtd_mloop

        sub.w   (a0),d2                  ; day of month
        addq.w  #1,d2                    ; 1..31

rtd_pday
        lsl.w   #5,d1
        or.w    d0,d1                    ; add in month * 2
        lsl.w   #4,d1
        or.w    d2,d1                    ; add in day

; now the time

        swap    d1
        moveq   #0,d2
        move.w  d1,d2
        divu    #60*30,d2                ; hours in lower d2
        move.w  d2,d1
        clr.w   d2
        swap    d2                       ; 2 seconds
        divu    #30,d2
        lsl.w   #6,d1                    ; move hours up
        or.b    d2,d1                    ; add minutes
        swap    d2
        lsl.w   #5,d1                    ; move minutes up
        or.b    d2,d1                    ; add 2 seconds


        moveq   #0,d0
        movem.l (sp)+,rtd.reg
rtd_rts
        rts

        end
