*  Long integer in d1 to decimal   V1.0    1984/1985  Tony Tebby  QJUMP
*
        section exten
*
        xdef    dec_cstrg
        xdef    dec_cfill
        xdef    dec_ov
        xdef    dec_stuff
*
*  a1 is pointer to start of string, d5 is ndp, d6 is field, d7 is comma set
*  d4 is DP
*
dec_stuff
        cmp.l   a1,a2           field full?
        ble.s   dec_ov4
        subq.l  #1,a2           one more character
        move.b  d0,(a6,a2.l)    ... into buffer
        rts
*
dec_ov4
        addq.l  #4,sp           remove return
dec_ov
        moveq   #$2a,d3         fill with '*'
        bsr.s   dec_sstrg
        move.l  a1,a2           set field full
        rts
*
dec_sstrg
        move.l  d6,d0
        move.l  a1,a2
        bra.s   dec_s_elp
dec_s_loop
        move.b  d3,(a6,a2.l)    set string to d2 ' ' or '*' 
        addq.l  #1,a2
dec_s_elp
        dbra    d0,dec_s_loop
        rts
*
*
dec_cstrg
        moveq   #$20,d3         fill string with spaces
dec_cfill
        bsr.s   dec_sstrg
        add.w   d5,d7           adjust comma position
dec_sdig
        subq.w  #1,d7           check for comma
        bne.s   dec_next
        moveq   #3,d7
        move.b  d4,d0           get dp
        bchg    #1,d0           thousands separator is the other one
        bsr.s   dec_stuff
*
dec_next
        move.l  d1,d0           divide top end of number
        clr.w   d0
        swap    d0
        divu    #10,d0          by 10
        move.w  d0,d2           save result
        move.w  d1,d0           now divide top remainder + bottom end
        divu    #10,d0          by 10
        swap    d0              remainder is our digit
        add.b   #'0',d0         add ascii zero
        bsr.s   dec_stuff       and stuff it
        subq.w  #1,d5           was this the last dp?
        bne.s   dec_ndiv        ... no
        move.b  d4,d0           get dp
        bsr.s   dec_stuff       and stuff it
dec_ndiv
        move.w  d2,d0           new top end (new bottom is already in msw)
        move.l  d0,d1           back into d1
        swap    d1              the right way round
        bne.s   dec_sdig        if some more, carry on
        tst.w   d5              have we done all the dps + 1 in front
        bge.s   dec_sdig        ... no, carry on
        rts
        end
