* Network device driver   V1.0    1985  Tony Tebby  QJUMP
*
        section nd
*
        xdef    nd_test                 test for input byte
        xdef    nd_fbyte                fetch input byte
        xdef    nd_sbyte                send byte to output
*
        xdef    nd_io                   IO entry
        xdef    nd_open                 open entry
        xdef    nd_close                close entry
        xdef    nd_rechp                release common heap
*
        xref    nd_bcast                read broadcast
        xref    nd_read                 read a packet
        xref    nd_send                 send a packet
*
        xref    nd_rept                 do repeated op until timeout or break
*
        include dev8_dd_qlnd_keys
*
* Open net channel
*
nd_open
        subq.l  #6,sp                   make room for three parameters
        move.l  sp,a3                   point to them
        move.w  io.name,a2              and decode the name
        jsr     (a2)
        bra.s   ndo_exit                ... oops
        bra.s   ndo_exit                ... oops
        bra.s   ndo_do                  ... NET recognised
*
        dc.w    3,'NET'                 name is net
        dc.w    3                       two parameters
        dc.w    2,'OI'                  out (default), in
        dc.w    ' _',0                  default station 0 (broadcast)
        dc.w    ' _',0                  no buffer
*
ndo_do
        movem.w (sp)+,d4/d5/d6          get parameters
        cmp.w   #$3f,d5                 check destination in range 0 to 63
        bhi.s   nd_bp                   ... no
        move.l  #nd_end,d1              set minimum reservation
*
        subq.w  #2,d4                   type is -ve for output, 0 for input
        blt.s   ndo_alchp               ... output, allocate minimum block
        tst.w   d5                      is it receive broadcast?
        bne.s   ndo_alchp               ... no
        asl.l   #8,d6                   buffer size*256
        asl.l   #2,d6                   buffer size*2
        bgt.s   ndo_albuf               ... just add to channel block
*
        move.l  sv_basic(a6),d6         no buffer specified, just take all
        sub.l   sv_free(a6),d6
        sub.l   #2048,d6                but 2k of free memory
        blt.s   ndo_alchp               (not that much there)
*
ndo_albuf
        add.l   d6,d1                   add buffer to channel block
ndo_alchp
        move.w  mm.alchp,a2             allocate in common heap
        jsr     (a2)
        bne.s   ndo_rts
*
        move.b  d5,nd_dest(a0)          set destination
        move.b  sv_netnr(a6),nd_self(a0) ... and me
        move.b  d4,nd_type(a0)          set type
        blt.s   ndo_rts                 ... output, done
*
        tst.w   d5                      was it broadcast?
        beq.l   nd_bcast                ... yes, read broadcast
        rts                             ... no, return
*
ndo_exit
        addq.l  #6,sp                   remove parameters
ndo_rts
        rts
        page
*
* Close net channel
*
nd_close
        tst.b   nd_type(a0)             is it output
        bge.s   nd_rechp                ... no, release space
        move.b  #1,nd_type(a0)          set eof
*
        lea     nd_send(pc),a2          set address of operation
        bsr.l   nd_rept                 and repeat until done
*
nd_rechp
        move.w  mm.rechp,a2             release channel block
        jmp     (a2)
        page
nd_bp
        moveq   #err.bp,d0
        rts
nd_ef
        moveq   #err.ef,d0
        rts
*
* Network IO
*
nd_io
        pea     ndd_test(a3)            push pseudo return address
        move.w  io.serio,a2             and do serial IO
        jmp     (a2)
*
* Fetch a byte
*
nd_fbyte
        bsr.s   nd_test                 test if it is there
        bne.s   ndio_rts                ... no
        move.l  a3,nd_rpnt(a0)          move pointer on
nd_ok
        moveq   #0,d0                   ok
ndio_rts
        rts
*
* Test if byte is there
*
nd_test
        move.b  nd_type(a0),d3          is it the right type?
        blt.s   nd_bp
        move.l  nd_rpnt(a0),a3          get pointer to buffer
        cmp.l   nd_epnt(a0),a3          is it at the end?
        beq.s   ndf_empty               ... yes
        move.b  (a3)+,d1                set next byte
        bra.s   nd_ok                   done
*
ndf_empty
        tst.b   d3                      buffer empty, is it eof?
        bgt.s   nd_ef                   ... yes
        bsr.l   nd_read                 read next packet
        bne.s   ndio_rts
        bra.s   nd_test                 and test again
*
* send byte
*
nd_sbyte
        tst.b   nd_type(a0)             is it output
        bge.s   nd_bp                   ... no
        moveq   #0,d2
        move.b  nd_nbyt(a0),d2          get next byte pointer
        addq.b  #1,d2                   move on
        bcc.s   nd_bput                 ... there is room
*
        sf      nd_type(a0)             set type not eof
        bsr.l   nd_send                 send packet
        st      nd_type(a0)             set type to send
        bne.s   ndio_rts               (positive means busy)
        moveq   #1,d2                   reset byte pointer
*
nd_bput
        move.b  d1,nd_data-1(a0,d2.w)   d2 already incremented
        move.b  d2,nd_nbyt(a0)
        bra.s   nd_ok
        end
