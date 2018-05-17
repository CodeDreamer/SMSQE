* Find job information  V0.0    1985  Tony Tebby   QJUMP
*
        section utils
*
        xdef    ut_fjob                 find a job (given the name)
        xdef    ut_jinf                 get job information
*
        xref    ut_cnmar                compare absolute and relative names
*
        include dev8_sbsext_ext_keys
*
ut_fjob
        moveq   #0,d1                   start from job 0
utj_loop
        move.l  a1,-(sp)                save name pointer
        bsr.s   ut_jinf                 get Job information
        move.l  (sp)+,a1
        bne.s   ut_fjob                 ... oops, try again
        cmp.w   #$4afb,(a0)+            check flag
        bne.s   utj_cknx
        bsr.l   ut_cnmar                compare names
        beq.s   utj_rts                 ... name found
utj_cknx
        move.l  d5,d1                   restore next job id
        bne.s   utj_loop                ... and check name of next job
        moveq   #err.nj,d0              job not found
utj_rts
        rts
*
ut_jinf
        move.l  d1,d4                   save Job ID
        moveq   #mt.jinf,d0             get Job information
        moveq   #0,d2                   scan whole tree
        trap    #1
        move.l  d1,d5                   save next Job ID
        addq.l  #6,a0                   move to flag
        tst.l   d0                      check errors
        rts
        end
