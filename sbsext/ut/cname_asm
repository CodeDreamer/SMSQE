* Compare names  V0.1    1985  Tony Tebby    QJUMP
*
        section utils
*
        xdef    ut_cnama                compare names at absolute addresses
        xdef    ut_cnmar                compare names a0 abs, a1 rel a6
*
*       d0   s
*       d1   s
*       d2   s
*       a0 c p  pointer to string
*       a1 c p  pointer to string 
*
* a0 absolute,  a1 relative
*
ut_cnmar
        move.w  sr,d0
        trap    #0                      and go to supervisor mode
        move.w  d0,-(sp)
        add.l   a6,a1                   a1 absolute
        bsr.s   ut_cnama                compare
        sub.l   a6,a1                   a1 relative
        sne     d0
        move.w  (sp)+,sr                restore sr
        tst.b   d0                      and return cc
        rts
*
* absolute addresses
*
ut_cnama
        movem.l a0/a1,-(sp)             save name pointers
        moveq   #0,d0
        addq.l  #1,a0                   (byte length only)
        move.b  (a0)+,d0                get string lengths
        addq.l  #1,a1
        cmp.b   (a1)+,d0
        bra.s   cn_lend
cn_loop
        bsr.s   cn_uc                   get one in upper case
        bsr.s   cn_uc                   and the other
        cmp.b   d1,d2                   the same?
cn_lend
        dbne    d0,cn_loop              next character
cn_exit
        movem.l (sp)+,a0/a1
        rts
*
cn_uc
        exg     a0,a1                   swap pointers
        move.b  d1,d2                   save old byte
        move.b  (a1)+,d1                get new one
        cmp.b   #'a',d1                 is it 'a'
        blo.s   uc_rts
        cmp.b   #'z',d1                 ... to 'z'
        bls.s   uc_set
        cmp.b   #$80,d1                 is it 'a umlaut'
        blo.s   uc_rts
        cmp.b   #$8b,d1                 ... to 'oe'
        bhi.s   uc_rts
uc_set
        bchg    #5,d1                   set upper case
uc_rts
        rts
        end
