* Network server driver   V0.0    1985  Tony Tebby  QJUMP
*
        section nd
*
        xdef    ns_io                   IO entry
        xdef    ns_open                 open entry
*
        xref    nd_read0                read a packet
        xref    nd_send0                send a packet
*
        include dev8_dd_qlnd_keys
*
* Open nsv channel
*
ns_open
        sub.l   a3,a3                   point to parameters!!
        move.w  io.name,a2              and decode the name
        jsr     (a2)
        bra.s   nso_exit                ... oops
        bra.s   nso_exit                ... oops
        bra.s   nso_do                  ... NET recognised
*
        dc.w    3,'NSV'                 name is nsv
        dc.w    0                       no parameters
*
nso_do
        move.l  #nd_fend,d1             allocate large net block
        move.w  mm.alchp,a2             allocate in common heap
        jsr     (a2)
        bne.s   nso_rts
*
        moveq   #$ffffff80,d5           set msb of station
        or.b    sv_netnr(a6),d5
        move.b  d5,nd_self(a0)          set myself
nso_exit
nso_rts
        rts
*
* Network IO
*
ns_io
        lea     nd_data(a0),a1          set block pointer
        subq.b  #io.fstrg,d0            fetch?
        beq.s   ns_read                 ... yes, read packet
        move.l  a2,d1                   find length of packet
        sub.l   a1,d1
        move.b  d2,nd_type(a0)          set type
        addq.b  #-nf.sblk,d2            is it send block?
        bne.s   ns_send                 ... no
        addq.w  #3,d1                   ... yes, length in long words
        lsr.w   #2,d1
ns_send
        move.b  d1,nd_nbyt(a0)          set length
        bra.l   nd_send0                then send packet
*
ns_read
        move.b  nd_self(a0),nd_dest(a0) read from anybody
        bsr.l   nd_read0                read packet
        move.b  nd_type(a0),d1          set type
        rts
        end
