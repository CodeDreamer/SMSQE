* SMS2 calendar table: universal bits   v0.00   Jul 1988  J.R.Oakley  QJUMP
*
        section calendar
*
        xdef    cv_nultb
        xdef    cv_montb
        xdef    cv_jleap
        xdef    cv_gleap
        xdef    cv_jdivt
        xdef    cv_gdivt
*+++
* Standard null table
*---
cv_nultb
        dc.w    0                       ; no (more) discontinuities
*+++
* Table of days in month, February being leap
*---
cv_montb
        dc.w    12                      ; twelve months
        dc.b    31,28+128,31,30,31,30   ; leap in February
        dc.b    31,31,30,31,30,31
*+++
* The leap day calculation for the Julian calendar: the Gregorian is an
* alternate entry point, called GLEAP.
*
*       Registers:
*               Entry                           Exit
*       D0      month (1..12)                   preserved
*       D2      year                            preserved
*       D3                                      number of leap days
*---
cv_jleap
        move.l  d2,d1                   ; just check year
        bra.s   chk4
cv_gleap
        move.l  d2,d1
        divu    #100,d1                 ; split into year/century
        swap    d1                      ; get year
        tst.w   d1                      ; is this a centennial?
        bne.s   chk4                    ; no
        swap    d1                      ; yes, check century for DIV4ness
chk4
        moveq   #0,d3                   ; assume not leap
        and.b   #3,d1                   ; is the thing divisible by 4?
        bne.s   cle_engl                ; no
        moveq   #1,d3                   ; yes, just one leap day
cle_engl
        rts
*
cv_jdivt
        dc.w    2
        dc.l    1461,2939745,4
        dc.l    365,3,1
*
cv_gdivt
        dc.w    4
        dc.l    146097,6000000,400
        dc.l    36524,3,100
        dc.l    1461,25,4
        dc.l    365,3,1
*
        end
