* Network remote file  IO  V0.5    1985   Tony Tebby   QJUMP
*
        section nd
*
        xdef    nf_io                   network file io
        xdef    nu_io                                io using name
        xdef    nf_test                              test input
        xdef    nf_fbyte                             fetch byte
        xdef    nf_sbyte                             send byte
*
        xdef    nf_eof                  set eof
*
        xref    nf_wblok                write the block in the buffer
        xref    nf_send                 send a packet
        xref    nf_sendw                send a packet and wait
        xref    nf_setky                set key and ID in packet
        xref    nf_setact               set key, ID, d1 and d2
        xref    nf_setln                set length of packet
*
        xref    nf_chkrd                check for read
        xref    nf_chkwr                check for write
*
        xref    nf_name                 name of net device ('N')
        xref    err_bp
*
        include 'dev8_mac_assert'
        include dev8_dd_nd_keys
*
* remote IO operations
*
nu_io
        lea     -ndd.lenf(a3),a4        distinguish linkage addresses
        exg     a3,a4
        bra.s   nfu_io
nf_io
        move.l  a3,a4                   only one linkage address
nfu_io
        move.l  a0,-(sp)                save address of channel
        move.l  nf_chblk(a0),a0         set true channel block
        bsr.s   nf_do_io                do IO
        move.l  (sp)+,a0                restore channel address
        rts
*
nf_do_io
        assert  nf_sonly+1,nf_eoff
        tst.w   nf_sonly(a0)            do we have EOF?
        bne.s   nf_doios
        movem.l d0-d3/a1,-(sp)
        moveq   #fs.posab,d0
        move.l  #$ffffbf,d1             set to eof
        bsr.l   nf_setact               set action
        bsr.l   nf_sendw                and do it
        bne.s   nf_dores
        st      nf_eoff(a0)             ... we have eof
        addq.l  #4,a5
        move.l  (a5)+,d1                ... this is it
        cmp.l   nf_eofpt(a0),d1         beyond our own?
        blt.s   nf_dores
        move.l  d1,nf_eofpt(a0)
nf_dores
        movem.l (sp)+,d0-d3/a1

nf_doios
        move.l  d0,d4                   save operation key
        subq.w  #io.edlin,d0            is it input?
        beq.s   nf_ed                   ... yes, edit line!
        blt.s   nf_in                   ... yes, normal
        subq.w  #io.sstrg-io.edlin,d0   is it output?
        bgt.l   nfio_fop                ... no, fancy io
nf_out
        tst.b   nf_acces(a0)            is output permitted?
        beq.s   nfio_ro                 ... no
        bsr.l   nf_chkwr                check the block (for write)
        beq.s   nf_serio
        bne.s   nfio_rts                do try to write into invalid block
nf_ed
        moveq   #io.fline,d4            pretend edlin in fline!
nf_in
        bsr.l   nf_chkrd                check the block (for read)
        bne.s   nfio_rts                ... not read (try op anyway for io.test)
nf_serio
        move.l  d4,d0                   set operation key
        pea     ndd_fser(a3)            push pseudo return address
        move.w  io.serio,a2             and do serial IO
        jmp     (a2)
nfio_ro
        moveq   #err.ro,d0
        rts
*
nf_test
        moveq   #0,d1                   position to here to force eof check
        bsr.s   nff_posre
*
nf_tsti
        move.l  nf_bfpnt(a0),d6         get pointer to next byte
        cmp.l   nf_bffil(a0),d6         within buffer?
        blt.s   nft_get                 ... yes
        tst.b   nf_eoff(a0)             eof set?
        beq.s   nf_nc                   ... no
        cmp.l   nf_eofpt(a0),d6         within file?
        bge.s   nf_eof
nf_nc
        moveq   #err.nc,d0              say nc to force reentry
nfio_rts
        rts
nft_get
        sub.l   nf_bfbot(a0),d6         get pointer to buffer
        move.b  nf_buffr(a0,d6.w),d1    get next byte
nf_ok
        moveq   #0,d0
        rts
nf_eof
        moveq   #err.ef,d0              end of file
        rts
nf_fbyte
        bsr.s   nf_tsti                 test if byte available
        blt.s   nfio_rts
        addq.l  #1,nf_bfpnt(a0)         move pointer on
        rts
*
nf_sbyte
        move.l  nf_bfpnt(a0),d6         get next pointer
        cmp.l   nf_bftop(a0),d6         off end of buffer?
        bge.s   nf_nc                   ... yes, retry
*
        move.l  d6,d0
        sub.l   nf_bfbot(a0),d0         get pointer to buffer
        move.b  d1,nf_buffr(a0,d0.w)    set byte
        st      nf_valid(a0)            set buffer modified
        addq.l  #1,d6                   move pointer on
        move.l  d6,nf_bfpnt(a0)         set running pointer
        cmp.l   nf_bffil(a0),d6         beyond fill pointer?
        ble.s   nf_ok                   ... no
        move.l  d6,nf_bffil(a0)         ... yes, reset fill pointer
        cmp.l   nf_eofpt(a0),d6         beyond eof?
        ble.s   nf_ok                   ... no
        move.l  d6,nf_eofpt(a0)         ... yes, reset eof
        bra.s   nf_ok
        page
*
* Fancy IO operations
*
nfio_fop
        moveq   #-fs.posab,d0           check if it is a position call
        add.l   d4,d0
        blt.s   nff_sel                 ... no, select other action
        beq.s   nff_posab               ... absolute
        subq.l  #fs.posre-fs.posab,d0
        bne.s   nff_sel                 ... no kind of position
        tst.w   d3                      posre, is it reentry?
        bne.s   nff_posab               ... yes
nff_posre
        add.l   nf_bfpnt(a0),d1         relative position
nff_posab
        tst.l   d1                      is position off start?
        bgt.s   nfp_ser                 ... no
        moveq   #0,d1                   ... yes, set to start 
        bra.s   nfp_sd1                 and return
nfp_ser
        tst.b   nf_sonly(a0)            is it serial only?
        bne.l   err_bp                  ... yes
nfp_ceof
        cmp.l   nf_eofpt(a0),d1         is it beyond current eof?
        ble.s   nfp_sd1                 ... no

nfp_eof
        move.l  nf_eofpt(a0),d1         set to end of file
        moveq   #err.ef,d0
nfp_sd1
        move.l  d1,nf_bfpnt(a0)         set pointer
nfp_rts
        rts
*
nff_sel
        bsr.l   nf_wblok                write out block so all is up to date
        blt.s   nff_rts
        moveq   #-sd.extop,d0           what next?
        add.w   d4,d0
        beq.l   err_bp                  ... extop, can't do
        sub.w   #sd.wdef-sd.extop,d0    set window?
        beq.s   nf_wlump                ... yes, send lump (a1)
        sub.w   #sd.recol-sd.wdef,d0    recolour?
        beq.s   nf_wlump                ... yes, send lump (a1)
        subq.w  #sd.fill-sd.recol,d0    fill or graphics?
        blt.s   nff_fs                  ... no
        subq.w  #sd.gcur-sd.fill,d0
        ble.s   nf_wlump                ... yes, send lump (a1)
nff_fs
        moveq   #-fs.flush,d0           check if a filing system op
        add.l   d4,d0
        beq.s   nf_flush                flush ignores err.bp
        subq.l  #fs.heads-fs.flush,d0
        cmp.w   #fs.trunc-fs.heads,d0   out of range?
        bhi.s   nf_action               ... yes
        add.w   d0,d0                   ... no, jump to it
        lea     nfio_act(pc,d0.w),a2
        add.w   (a2),a2
        jmp     (a2)
nfio_act
        dc.w    nf_wlump-*      heads
        dc.w    nf_action-*     headr
        dc.w    nf_in-*         load
        dc.w    nf_out-*        save
        dc.w    nf_renam-*      rename
        dc.w    nf_trunc-*      truncate
nf_flush
        bsr.s   nf_action               do normal action
        moveq   #err.bp,d3              check if err.bp
        cmp.l   d0,d3
        bne.s   nff_rts                 ... no it isn't
nff_ok
        moveq   #0,d0                   set the return OK
nff_rts
        rts
*
nf_action
        bsr.s   nff_setact              just send ID and action regs
nfio_send
        bsr.l   nf_send                 send packet and get reply
        bne.s   nfio_sbad
        move.l  (a5)+,d0                set return code
        move.l  (a5)+,d1                d1
        move.l  a1,a2                   get a1
        add.l   (a5)+,a1                set change to a1
        moveq   #-$c,d2                 set number of bytes to copy
        add.b   nd_nbyt(a0),d2
        ble.s   nfio_sd0                 ; no return
nfio_sloop
        move.b  (a5)+,(a2)+             set return string
        subq.b  #1,d2
        bgt.s   nfio_sloop
nfio_sbad
        sf      nf_valid(a0)            data block smashed
nfio_sd0
        tst.l   d0                       ; set status
        rts

nff_act_sm
        sf      nf_valid(a0)            data block smashed
nff_setact
        move.l  d4,d0
        bra.l   nf_setact               set up action
*
* send a lump from (a1)
*
nf_wlump
        bsr.s   nff_act_sm              set up action (data block smashed)
        moveq   #30,d0                  set 30 bytes (max is arc)
        move.l  a1,a2
nfwl_loop
        move.b  (a2)+,(a5)+
        dbra    d0,nfwl_loop
*
        bsr.l   nf_setln                set packet length
        bra     nfio_send                ; send packet
*
* Truncate file
*
nf_trunc
        move.l  nf_bfpnt(a0),d1         get current position
        move.l  d1,nf_eofpt(a0)         set eof
        st      nf_eoff(a0)
        cmp.l   nf_bffil(a0),d1         is fill beyond eof?
        bgt.s   nft_spos                ... no
        move.l  d1,nf_bffil(a0)         ... yes, reset fill
nft_spos
        moveq   #fs.posab,d4            set position
        bsr.s   nf_action
        bne.s   nff_rts
        moveq   #fs.trunc,d4
        bra.s   nf_action               and truncate
*
* Rename
*
nf_renam
        bsr.s   nff_act_sm              set up action (data block smashed)
        move.l  a1,d6                   do not smash a1
        addq.l  #2,a5                   leave room for name length
        moveq   #0,d7
        move.w  (a1)+,d7                set given name length
        add.l   a1,d7                   ... end of name
        lea     ndd_name(a3),a2         address of name
        moveq   #$ffffffdf,d3           mask of upper/lower
        move.w  (a2)+,d5                number of characters in name
nfr_cknam
        move.b  (a2)+,d1                character of name
        move.b  (a1)+,d2                character of string
        and.b   d3,d2                   upper case
        cmp.b   d1,d2
        bne.s   nf_bn                   bad name
        subq.w  #1,d5                   next character
        bgt.s   nfr_cknam
*
        move.w  nf_nnum(a0),d3          get this N number
        move.b  d3,d5
        add.b   #'0',d5                 check device number correct
        cmp.b   (a1)+,d5
        bne.s   nf_bn                   ... no
        cmp.b   #'_',(a1)+              is it followed by underscore?
        bne.s   nf_bn
*
        cmp.l   a3,a4                   is this a use?
        beq.s   nfr_cname               ... no
        lsl.w   #ndd.nusf,d3            set pointer to use
        lea     ndd_nudr-ndd.nuln(a3),a2
        add.w   d3,a2
        move.w  (a2)+,d5                length of use
        beq.s   nfr_cname
nfr_cuse
        move.b  (a2)+,(a5)+             copy use name
        subq.w  #1,d5
        bgt.s   nfr_cuse
*
nfr_cname
        cmp.l   a1,d7                   at end of name?
        ble.s   nfr_do                  ... yes
        move.b  (a1)+,(a5)+             ... no, copy name
        bra.s   nfr_cname
nfr_do
        move.l  d6,a1                   restore a1
        bsr.l   nf_setln                set length of block
        sub.w   #$e,d5                  and length of name
        move.w  d5,$c(a2)
        bra.l   nfio_send               send reply
*
nf_bn
        moveq   #err.bn,d0
        rts
        end
