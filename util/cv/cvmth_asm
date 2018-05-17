* Convert text month to number          v0.00   J.R.Oakley  Mar 1988  QJUMP
*
        section cv
*
        xref    cv_lctab
*
        xdef    cv_txtmn
        xdef    cv_mntxt
*+++
* Convert a string which should contain a text month name into
* a month number from 1 to 12.  Leading spaces only are permitted.
*
*       Registers:
*               Entry                           Exit
*       D1                                      month number
*       D7      end of text                     preserved
*       A0      start of text                   updated
*       A2      list of months (36 chars)       smashed
*---
cv_txtmn
*
cvmreg  reg     d0/d2-d6
        movem.l cvmreg,-(sp)
        move.w  #%1111111111110,d0      ; all months still valid
        moveq   #' ',d1
rmlspc
        cmp.l   a0,d7                   ; off end of buffer?
        ble.s   flsbit                  ; yes
        cmp.b   (a0)+,d1                ; is this a space?
        beq.s   rmlspc                  ; yes, ignore it
*
        subq.l  #1,a0                   ; no, go back to it
        addq.l  #2,a2                   ; point to first letter of "Jan"
        move.l  d7,d1                   ; we have...
        sub.l   a0,d1                   ; ...this many characters
        beq.s   flsbit                  ; none
        cmp.w   #2,d1                   ; more than two?
        bls.s   slctab                  ; no
        moveq   #2,d1                   ; yes, three letters to go
slctab 
        lea     cv_lctab(pc),a1         ; lower case table is here
        moveq   #0,d2                   ; clear out upper bits
        moveq   #0,d6
*
cmplp
        move.b  (a0)+,d2                ; character to check
        move.b  0(a1,d2.w),d2           ; lowercase it
        move.w  d0,d3                   ; get map of valid months
        moveq   #12,d4                  ; test twelve of them
        moveq   #11*3,d5                ; start with letter at this offset
letlp
        move.b  0(a2,d5.w),d6           ; get month letter
        cmp.b   0(a1,d6.w),d2           ; compare with it in lower case
        beq.s   letok                   ; same
        bclr    d4,d3                   ; different, this month won't do
letok
        subq.w  #1,d4                   ; next month
        subq.w  #3,d5                   ; and its Nth letter
        bpl.s   letlp                   ; there is one
        tst.w   d3                      ; any months left?
        beq.s   flsbit                  ; no, find first match
        move.w  d3,d0                   ; yes, carry on
        addq.l  #1,a2                   ; using next month letter
        dbra    d1,cmplp
flsbit
        moveq   #-1,d1                  ; start at month -1 (!)
flslp
        addq.w  #1,d1                   ; next month
        lsr.w   #1,d0                   ; is this bit
        bcc.s   flslp                   ; but it isn't valid
*
        movem.l (sp)+,cvmreg
        rts
*+++
* Convert a month number from 1 to 12 into the corresponding three-letter
* string.
*
*       Registers:
*               Entry                           Exit
*       D1      month number                    preserved
*       D7      end of buffer                   preserved
*       A0      buffer pointer                  updated
*       A2      list of months (36 chars)       smashed
*---
cv_mntxt
        movem.l cvmreg,-(sp)
        add.w   d1,a2
        add.w   d1,a2                   ; get offset in text
        subq.w  #1,d1                   ; including 2 chars for the length!
        add.w   d1,a2
        moveq   #2,d0
cpchar
        cmp.l   a0,d7                   ; off end of buffer?
        ble.s   flsbit                  ; yes
        move.b  (a2)+,(a0)+             ; no, copy char
        dbra    d0,cpchar
        movem.l (sp)+,cvmreg
        rts
*
        end

