* Convert day to year/month/day         v0.00   Mar 1988 J.R.Oakley  QJUMP
*
        section cv
*
        include 'dev8_mac_assert'
        include 'dev8_keys_err'
        include 'dev8_keys_caltab'
*
        xref    cv_uldiv
        xref    cv_ulmul
*
        xdef    cv_dnymd
        xdef    cv_ymndy
        xdef    cv_ymddn
*
year0   equ     -5877599
*+++
* Convert a day number into a year, month and day.  The conversion table
* is passed to allow for different calendars in different countries.
* Its format is as follows:
*
* word  number of sub-tables
* long  max. day number for this sub-table      )
* long  corresponding year...                   )
* word  ...month...                             ) N copies, with day number
* word  ...and day                              ) and year/month/day
* long  fudge factor                            ) increasing
* word  offset of discontinuity table           )
* word  offset of days-in-month table           )
* word  offset of leap-year checking code       )
* word  offset of division table                )
*
* A discontinuity table has the following format:
*
* word  number of discontinuities
* long  year of discontinuity                   )
* word  month                                   ) N copies, in increasing
* word  first missing day                       ) order
* word  number of missing days                  )
*
* The days-in-month table has the following format:
*
* word  number of months/year
* byte  number of days in month, +128 if leap   ) N of these
*
* Leap-year checking code is called with the month in D0, year in D2:
* it returns the number of leap days in D3.  D1 may be smashed.
*
* The division table has the following format:
*
* word  number of entries
* long  days in unit                            ) 
* long  max. years in unit                      ) N copies
* long  years in unit                           )
*
*       Registers:
*               Entry                           Exit
*       D0      conversion table                error code
*       D1.l    day number                      day (MSW) month (LSW)
*       D2                                      year
*---
cv_dnymd
*
cvdreg  reg     d3/d4/a0/a1
        movem.l cvdreg,-(sp)
*
* First find out which routine is to be used for the conversion: the table
* passed is of the format above, and should cover the entire range of
* 4 Gdays.
*
        move.l  d0,a0                   ; point to table
        move.w  (a0)+,d2                ; there are this many tables
        bra.s   cvd_ftbe
cvd_ftbl
        cmp.l   cdt_maxd(a0),d1         ; is this the right sub-table?
        bls.s   cvd_tabf                ; yes
        add.w   #cdt.tlen,a0            ; no, look at next entry
cvd_ftbe
        dbra    d2,cvd_ftbl
        bra.s   cvd_exor                ; day number not in table range ?!
*
* The correct conversion table is found, so do the conversion.
*
cvd_tabf
        sub.l   cdt_fudg(a0),d1         ; add the universal fudge factor
        lea     cdt_divt(a0),a1         ; this is the division table to use...
        add.w   (a1),a1                 ; ...here
        move.l  #year0,d2               ; start at year 0
        move.w  (a1)+,d3                ; there are this many divisions
        bra.s   divle
*
divll
        move.l  (a1)+,d0                ; by this
        jsr     cv_uldiv(pc)            ; divide
        cmp.l   (a1)+,d0                ; is quotient too big?
        ble.s   mult                    ; no
        move.l  -4(a1),d0               ; yes, set it to max
        add.l   -8(a1),d1               ; and add back some to remainder
mult
        move.l  d1,d4                   ; keep remainder safe
        move.l  (a1)+,d1                ; make...
        jsr     cv_ulmul(pc)            ; ...number of years
        add.l   d0,d2                   ; and add it in
        move.l  d4,d1                   ; restore remainder
divle
        dbra    d3,divll
        bsr.s   cvd_dnmd                ; convert year/day to year/month/day
cvd_exit
        movem.l (sp)+,cvdreg
        rts
cvd_exor
        moveq   #err.orng,d0
        bra.s   cvd_exit
*+++
* Convert a day number in a given year to a month/day
*
*       Registers:
*               Entry                           Exit
*       D0                                      error code
*       D1      day number                      day/month
*       D2      year                            preserved
*       D3/D4                                   ???
*       A0      conversion table entry          preserved
*       A1                                      ???
*---
cvd_dnmd
dnmdreg reg     d7/a2/a3
        movem.l dnmdreg,-(sp)
        moveq   #0,d0                   ; start at "month 0"
        bsr.s   cvd_sett                ; set up table pointers
cvd_subm
        sub.w   d4,d1                   ; knock off days last month
        bmi.s   cvd_adjs                ; too far, month is found
        bsr.s   cvd_fndy                ; find days in next month
        sub.w   d3,d4                   ; ignore any missing ones
        bra.s   cvd_subm
*
* We get here with D0=month, D1=day less max. day number, D3=discontinuity
* D4=max. day number.
*
cvd_adjs
        add.w   d4,d1                   ; make D1 day in month
        swap    d3                      ; get first missing day
        cmp.w   d1,d3                   ; is day number after that?
        bgt.s   cvd_idsc                ; no
        swap    d3                      ; yes, get missing days
        add.w   d3,d1                   ; date is that much bigger
cvd_idsc
        addq.w  #1,d1                   ; day is in 1..n
        swap    d1                      ; day to MSW
        move.w  d0,d1                   ; month to LSW
        movem.l (sp)+,dnmdreg
        rts
*
* Set up to find days in a particular month, or to convert a day number in
* a year into a month/day
*
cvd_sett
        lea     cdt_leap(a0),a3
        add.w   (a3),a3                 ; point to leap code
*
        lea     cdt_dyim(a0),a1         ; days in month table
        add.w   (a1),a1                 ; is here
        addq.l  #2,a1                   ; skip number of months
        add.w   d0,a1                   ; start at this month
        moveq   #0,d4                   ; last month had no days
*
        moveq   #0,d7                   ; assume no discontinuities
        lea     cdt_dsct(a0),a2         ; discontinuity table
        move.w  (a2),d7                 ; is this far along
        beq.s   cvd_ndsc                ; no discontinuities
        add.w   d7,a2                   ; table is here
        moveq   #-1,d7                  ; set "discontinuities" flag (D7 -ve)
        move.w  (a2)+,d7                ; this many entries left
cvd_ndsc
        tst.w   d7                      ; no (more) discontinuities?
        beq.s   cvd_nfds                ; no
        subq.w  #1,d7                   ; try the next one
        cmp.l   (a2)+,d2                ; is it in this year?
        beq.s   cvd_xset                ; yes
        addq.l  #cdt.dlen-cdt_dmth,a2   ; no, skip the rest of the entry
        bgt.s   cvd_ndsc                ; there may be another possibility
cvd_nfds
        moveq   #0,d7                   ; no more interesting discontinuities
cvd_xset
        rts
*
* Find number of days/discontinuity in a given month
*
*       Registers:
*               Entry                           Exit
*       D0      last month number               this month number
*       D2      year                            preserved
*       D3                                      discontinuity or 0
*       D4                                      days in this month
*       D7      discontinuity flag              updated if disc.
*       A1      month length table              updated
*       A2      next disc. table entry          updated if disc.
*       A3      lear calculation pointer        preserved
*
cvd_fndy
        addq.w  #1,d0                   ; this is the month number
        move.b  (a1)+,d4                ; there are this many days in it
        bpl.s   cvd_chkd                ; not leap, find discontinuity if any
        move.l  d1,-(sp)                ; save remaining days
        jsr     (a3)                    ; leap, how many days extra?
        move.l  (sp)+,d1
        bclr    #7,d4                   ; knock off leap flag
        add.w   d3,d4                   ; and add leap days
cvd_chkd
        moveq   #0,d3                   ; assume no discontinuity
        tst.l   d7                      ; any discontinuities?
        beq.s   cvd_xdim                ; no
        cmp.w   (a2),d0                 ; month of discontinuity?
        bne.s   cvd_xdim                ; no
        addq.l  #cdt_dfmd-cdt_dmth,a2   ; yes, skip month
        move.l  (a2)+,d3                ; get discontinuity data
        assert  cdt_dfmd+4,cdt.dlen
        bsr.s   cvd_ndsc                ; set up for next discontinuity
cvd_xdim
        rts
*+++
* Find number of days in a given month: the maximum day number is returned,
* and also any missing days.
*
*       Registers:
*               Entry                           Exit
*       D0      conversion table                max day number (1..n)
*       D1      month number (1..12)            1st missing day | days missing 
*       D2      year                            preserved
*---
cv_ymndy
dyimreg reg     d3/d4/d7/a0-a3
        movem.l dyimreg,-(sp)
        swap    d1
        move.w  #1,d1                   ; say we want the first of the month
        swap    d1
        bsr.s   cvy_setu                ; find the right sub-table
        move.w  d1,d0
        subq.w  #1,d0                   ; first month is 0
        bsr.s   cvd_sett                ; set pointers from that
        bsr.s   cvd_fndy                ; find number of days
        move.l  d3,d1                   ; return discontinuity
        move.w  d4,d0                   ; and max days
        movem.l (sp)+,dyimreg
        rts
*
* Set up to find out about year/month(/day) - find appropriate table
* entry.
*
cvy_setu
        move.l  d0,a0                   ; point to table of conversion tables
        move.l  d1,d0
        swap    d0                      ; get day
        move.w  (a0)+,d3                ; this many sub-tables
cvy_ftlp
        addq.l  #cdt_maxy-cdt_maxd,a0   ; skip maximum day number
        cmp.l   (a0)+,d2                ; year less than table year?
        blt.s   cvy_uad4                ; yes, can use the table
        bgt.s   cvy_smdy                ; no, try next entry
        cmp.w   (a0)+,d1                ; same, check month
        blt.s   cvy_uad2                ; less, use routine
        bgt.s   cvy_sday                ; more, next entry
        cmp.w   (a0)+,d0                ; same, check day
        ble.s   cvy_use                 ; less or equal, use routine
        bra.s   cvy_sall
cvy_smdy
        addq.l  #cdt_maxt-cdt_maxm,a0
cvy_sday
        addq.l  #cdt_fudg-cdt_maxt,a0
cvy_sall
        add.w   #cdt.tlen-cdt_fudg,a0
        bra.s   cvy_ftlp
cvy_uad4
        addq.l  #cdt_maxt-cdt_maxm,a0
cvy_uad2
        addq.l  #cdt_fudg-cdt_maxt,a0
cvy_use
        sub.w   #cdt_fudg,a0            ; point to start of table
        rts
*+++
* Convert year/month/day to day number: the conversion table format is
* as for cv_DNYMD.
*
*       Registers:
*               Entry                           Exit
*       D0      conversion table                preserved
*       D1      day/month                       day number
*       D2      year                            preserved
*---
cv_ymddn
cvyreg  reg     d0/d2-d5/d7/a0-a3
        movem.l cvyreg,-(sp)
        bsr     cvy_setu                ; find the right table
        move.l  cdt_fudg(a0),d5         ; get day fudge factor
*
        moveq   #0,d0
        bsr     cvd_sett                ; set up month pointer etc.
cvy_addm
        bsr     cvd_fndy                ; number of days in next month
        cmp.w   d0,d1                   ; this month?
        beq.s   cvy_thsm                ; yes
        sub.w   d3,d4                   ; omit missing days
        ext.l   d4
        add.l   d4,d5                   ; and add to total
        bra.s   cvy_addm
cvy_thsm
        swap    d1                      ; get day
        swap    d3                      ; and first missing
        cmp.w   d1,d3                   ; is day after first missing?
        bgt.s   cvy_addd                ; no, just add day number
        swap    d3                      ; yes, get missing days
        sub.w   d3,d1                   ; and knock 'em off
cvy_addd
        subq.w  #1,d1                   ; get day in 0..n-1
        ext.l   d1                      ; that's long
        add.l   d1,d5                   ; and up the total
*
        lea     cdt_divt(a0),a1         ; division table
        add.w   (a1),a1                 ; is here
        add.l   #-year0,d2              ; adjust year to unsigned
        move.l  d2,d1
        move.w  (a1)+,d3                ; count of calculations
        bra.s   mule
mull
        move.l  cdt_unyr(a1),d0         ; number of years in N days
        jsr     cv_uldiv(pc)
        move.l  d1,d2                   ; save remainder
        assert  cdt_undy,0
        move.l  (a1)+,d1                ; days in N years
        jsr     cv_ulmul(pc)            ; calculate it
        add.l   d0,d5                   ; and add it in
        move.l  d2,d1                   ; remaining years
        addq.l  #cdt.ulen-cdt_umxy,a1   ; skip to next table entry
mule
        dbra    d3,mull
        move.l  d5,d1                   ; return result
        movem.l (sp)+,cvyreg
        rts
*
        end
