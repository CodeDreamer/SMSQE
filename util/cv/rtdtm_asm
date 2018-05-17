; Convert real time to date and time       V0.00   1993 Tony Tebby

        section cv

        xdef    cv_rtdtm

        xref    cv_mnths

;+++
; Convert real time to date and time
;
;       d1 c  p real time
;       a1 c  p pointer to date and time buffer
;
;       status return 0
;---
cv_rtdtm
rtd.reg reg     d2/d3/a0/a1

        movem.l rtd.reg,-(sp)

        moveq   #0,d3
        lsr.l   #1,d1
        roxl.w  #1,d3                    ; odd second in d3
        divu    #24*60*30,d1             ; split into days / 2 seconds

; first do the days

        moveq   #0,d0
        move.w  d1,d0
        divu    #365*4+1,d0              ; four year cycle
        move.l  d0,d2
        clr.w   d2
        swap    d2                       ; four year day
        lsl.w   #2,d0                    ; years

        cmp.w   #3*365+31+28,d2          ; is it Feb 29 of leap year?
        blt.s   rtd_day
        bgt.s   rtd_day1

        move.w  d0,d1                    ; four year base
        addq.w  #3,d1                    ; first leap year
        moveq   #2,d0                    ; leap month
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
        addq.w  #3,d0                    ; first guess at month

        lea     cv_mnths-2(pc),a0
        add.w   d0,a0
        add.w   d0,a0
rtd_mloop
        subq.w  #1,d0
        cmp.w   -(a0),d2                 ; start less than month?
        blt.s   rtd_mloop

        sub.w   (a0),d2                  ; day of month
        addq.w  #1,d2                    ; 1..31

rtd_pday
        add.w   #1961,d1                 ; base year
        move.w  d1,(a1)+                 ; year
        move.b  d0,(a1)+                 ; month
        move.b  d2,(a1)+                 ; day
        st      (a1)+                    ; day of week

; now the time

        clr.w   d1
        swap    d1
        divu    #60*30,d1                ; hours in lower d1
        move.b  d1,(a1)+
        clr.w   d1
        swap    d1                       ; 2 seconds
        divu    #30,d1                   ; minutes in lower d1
        move.b  d1,(a1)+
        swap    d1
        add.w   d1,d1                    ; seconds
        add.w   d3,d1                    ; add back odd second
        move.b  d1,(a1)+

        moveq   #0,d0
        movem.l (sp)+,rtd.reg
rtd_rts
        rts

        end
