; Calculate single parts of a given date      1991 Jochen Merz

        section utility

        xdef    ut_cyear        ; returns year
        xdef    ut_cmon         ; returns month
        xdef    ut_cday         ; returns day
        xdef    ut_chour        ; returns hour
        xdef    ut_cmin         ; returns minute
        xdef    ut_csec         ; returns second
        xdef    ut_cweek        ; returns week

        xref    ut_gsdat
        xref    cv_fdate
        xref    cv_decil
        xref    cv_txtmn

        xref    fmt_year,fmt_mon,fmt_day,fmt_hour,fmt_min,fmt_sec,fmt_mons

utc_reg reg     a0-a2
;+++
; All routines work in the same way:
;
;               Entry                   Exit
;       D1.l    standard date           value requested
;---
ut_cyear
        movem.l utc_reg,-(sp)
        lea     fmt_year,a2
        bra.s   calc_all
ut_cday
        movem.l utc_reg,-(sp)
        lea     fmt_day,a2
        bra.s   calc_all
ut_chour
        movem.l utc_reg,-(sp)
        lea     fmt_hour,a2
        bra.s   calc_all
ut_cmin
        movem.l utc_reg,-(sp)
        lea     fmt_min,a2
        bra.s   calc_all
ut_csec
        movem.l utc_reg,-(sp)
        lea     fmt_min,a2
calc_all
        sub.l   #10,sp          ; make room for string
        move.l  sp,a1           ; that's the buffer now
        bsr     cv_fdate        ; format date
        move.l  sp,a0           ; base of string
        move.l  sp,d7
        addq.l  #2,d7
        add.w   (a0)+,d7        ; end of string
        bsr     cv_decil        ; convert ascii to number
ret_all
        add.l   #10,sp
        movem.l (sp)+,utc_reg
        rts

ut_cmon
        movem.l utc_reg,-(sp)
        lea     fmt_mon,a2
        sub.l   #10,sp          ; make room for string
        move.l  sp,a1           ; that's the buffer now
        bsr     cv_fdate        ; format date
        move.l  sp,a0           ; base of string
        move.l  sp,d7
        addq.l  #2,d7
        add.w   (a0)+,d7        ; end of string
        lea     fmt_mons,a2
        bsr     cv_txtmn        ; get month number
        bra.s   ret_all

ut_cweek
        movem.l d0/d2-d6,-(sp)
        move.l  d1,d6           ; keep date
        bsr     ut_cyear        ; get year
        move.l  d1,d0           ; year must be d0
        moveq   #1,d1           ; january
        moveq   #1,d2           ; 1st
        moveq   #0,d3           ; 00:00:00
        moveq   #0,d4
        moveq   #0,d5
        bsr     ut_gsdat        ; that's the first day this year
        move.l  d1,d0
        moveq   #0,d1           ; week count
cwk_loop
        cmp.l   d0,d6           ; within this week?
        bls.s   cwk_rts
        add.l   #60*60*24*7,d0  ; next week .. in seconds
        addq.b  #1,d1           ; .. and weeks
        cmp.b   #54,d1          ; maximum weeks of a year
        bls.s   cwk_loop
cwk_rts
        movem.l (sp)+,d0/d2-d6
        rts

        end
