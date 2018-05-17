* Write out bits and pieces    Tony Tebby   QJUMP
*
        section utils
*
        xdef    ut_trap3
        xdef    ut_trp3r
        xdef    ut_wrta1
        xdef    ut_wrtst
        xdef    ut_wrtnl
        xdef    ut_wrtch
*
        include dev8_sbsext_ext_keys
*
ut_wrtnl
        moveq   #$a,d1                  newline
ut_wrtch
        moveq   #io.sbyte,d0            send one character
        bra.s   ut_trap3
*
ut_wrta1
        move.w  (a6,a1.l),d2            get character count
        addq.l  #2,a1                   move pointer on
ut_wrtst
        moveq   #io.sstrg,d0            send string
*
* trap #4 then trap #3
*
ut_trp3r
        trap    #4                      relative a6
*
* trap #3 preserving d3
*
ut_trap3
        move.l  d3,-(sp)                save d3
        moveq   #-1,d3                  set infinite timeout
        trap    #3                      do trap
        move.l  (sp)+,d3                restore d3
        tst.l   d0                      test error return
        rts
        end
