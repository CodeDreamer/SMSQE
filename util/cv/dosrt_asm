; Convert DOS time to real time       V0.00   1993 Tony Tebby

        section cv

        xdef    cv_dosrt

        xref    cv_mnths

;+++
; Convert DOS time to real time
;
;       d1 cr  dos time / real time
;       status return 0
;---
cv_dosrt
drt.reg reg     d2/d3
        move.l  d1,d0
        beq.s   drt_rts

        movem.l drt.reg,-(sp)

        swap    d1                       ; work on day
        move.w  d1,d2
        lsr.w   #8,d2
        lsr.w   #1,d2                    ; year since 1980
        move.w  d2,d0                    ; keep it
        mulu    #365,d0                  ; day
        lsr.w   #2,d2
        add.w   d0,d2                    ; corrector for leap year
        moveq   #%11111,d0
        and.w   d1,d0
        add.w   d0,d2                    ; + day
        move.w  #%111100000,d0
        and.w   d1,d0
        lsr.w   #4,d0
        move.w  cv_mnths-2(pc,d0.w),d0
        add.w   d0,d2                    ; + start of month
        and.w   #%11111100000,d1         ; 2 lsbits of year + month
        cmp.w   #%00001000000,d1         ; January / February in leap year
        bgt.s   drt_base                 ; ... no
        subq.w  #1,d2                    ; yes, we've gone too far

drt_base
        add.w   #19*365+4,d2             ; adjust base to 1961

; the day is done, now the time

        swap    d1
        move.w  #%1111100000000000,d0
        and.w   d1,d0                    ; hours * 64 * 32
        move.w  d0,d3
        lsr.w   #4,d0                    ; hours * 4 * 32
        sub.w   d0,d3                    ; hours * 60 * 32

        move.w  #%11111100000,d0
        and.w   d1,d0                    ; minutes * 32
        add.w   d0,d3                    ; ((hours * 60) + minutes) * 32
        move.w  d3,d0
        lsr.w   #4,d0                    ; ... * 2
        sub.w   d0,d3                    ; ((hours * 60) + minutes) * 30
        and.l   #%11111,d1
        add.w   d3,d1                    ; time of day in 2 second units

        mulu    #24*60*30,d2             ; date in 2 second units
        add.l   d2,d1
        add.l   d1,d1                    ; real time

        moveq   #0,d0
        movem.l (sp)+,drt.reg
drt_rts
        rts
        end
