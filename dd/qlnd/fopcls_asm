* Network remote filing OPEN/CLOSE  V0.4    1985   Tony Tebby   QJUMP
*
        section nd
*
        xdef    nf_open                 network file open
        xdef    nu_open                              open with use
        xdef    nf_close                             close
        xdef    nu_close
        xdef    nf_formt                             format (in use)
*
        xref    nf_setwr                set up to write block
        xref    nf_rstwr                reset pointers afterwards
        xref    nf_setky                set key and ID in packet
        xref    nf_setact               set key and ID only
        xref    nf_setln                set length of packet
        xref    nf_sendw                send packet and wait
        xref    nd_rechp                release common heap
*
*$$     xref    prt_name                name of printer device
*
        include dev8_dd_qlnd_keys
*
nu_open
        lea     -ndd.lenf(a3),a5        set pointer to real linkage block
        bra.s   nfu_open
nf_open
        move.l  a3,a5                   set pointer to linkage
nfu_open
        move.l  a0,a4                   ... and pointer to channel
        moveq   #0,d6
        move.b  fs_drivn(a1),d6         keep drive number (word)
        bsr.s   nfo_do
nfu_exit
        move.l  a4,a0                   restore channel
        rts

nfo_do
        move.l  #nd_fend,d1             allocate network channel block
        move.l  a3,-(sp)                saving a3
        move.w  mm.alchp,a2
        jsr     (a2)
        move.l  (sp)+,a3
        bne.s   nfo_exit                oops
        move.l  a0,nf_chblk(a4)         set address of channel block
*
        move.b  d6,nf_nnum+1(a0)        set N number (word)
*
        lea     nd_data+3(a0),a1        start to fill the data block
        move.b  fs_acces(a4),d3         get access type
        move.b  d3,d4                   (save it)
        move.b  d3,(a1)+                ... and set
        subq.b  #io.share,d3            set share to zero
        beq.s   nfo_use
        subq.b  #io.dir-io.share,d3     set dir to zero
        sne     nf_acces(a0)            ... and set flag
*
nfo_use
        addq.l  #2,a1                   skip past name length
        move.l  a1,a2                   save start of name
        cmp.l   a3,a5                   check if a 'use' in operation
        beq.s   nfo_name                ... no
        lsl.w   #ndd.nusf,d6                   ... yes, index use table by drive number
        lea     ndd_nunr-ndd.nuln(a5,d6.w),a3
        move.w  (a3)+,d6                get net node number
        move.w  (a3)+,d0                and length of use name
*
nfo_usel
        move.b  (a3)+,(a1)+             copy use name
        subq.w  #1,d0
        bgt.s   nfo_usel
*
nfo_name
        lea     fs_fname(a4),a3         now copy characters of name
        move.w  (a3)+,d0
        bra.s   nfo_nend
nfo_naml
        move.b  (a3)+,(a1)+
nfo_nend
        dbra    d0,nfo_naml
*
        sub.l   a2,a1                   find length of name
        move.w  a1,-(a2)                and set it
*
* Open name copied, now set up header
*
        lea     nd_dest(a0),a2
        move.b  d6,(a2)                 set destination
        tas     (a2)+                   +$80
        move.b  sv_netnr(a6),(a2)+      set self
        addq.l  #6,a1                   set length of packet to include d3 and len
        move.l  a1,d1
        add.w   #io.open<<8,d1          set type to OPEN
        move.l  d1,(a2)                 set block number, type and length
*
        bsr.l   nf_sendw                send packet and get reply (waiting)
        bne.s   nfo_rel                 ... oops
        move.l  (a5)+,d0                get error return
        bne.s   nfo_rel
*
        tst.b   d4                      was it delete?
        blt.s   nfo_rel
*
        move.l  (a5)+,nf_chid(a0)       set channel ID
        move.b  (a5)+,nf_sonly(a0)      set serial ops only
        rts
*
nfo_rel
        move.l  d0,d4                   save error code
        bsr.s   nfoc_rel
        move.l  d4,d0                   reset error code
nfo_exit
        rts
        page
*
* Close file
*
np_close
*$$     movem.l d7/a4/a5,-(sp)          save regs
*$$     bsr.s   nfc_do                  do close
*$$     bra.s   nc_exit
*
nu_close
nf_close
        movem.l d7/a4/a5,-(sp)          save regs
        move.l  a0,a4
        move.l  nf_chblk(a4),a0         get true channel block
        bsr.s   nfc_do                  do close
*
        moveq   #0,d0
        move.b  fs_drive(a4),d0         find the physical definition
        lsl.b   #2,d0
        lea     sv_fsdef(a6),a2
        move.l  (a2,d0.w),a2
        subq.b  #1,fs_files(a2)         one fewer files
*
        lea     fs_next(a4),a0          unlink channel block
        lea     sv_fslst(a6),a1         ... from fs linked list
        move.w  ut.unlnk,a2
        jsr     (a2)
*
        move.l  a4,a0                   restore channel block address
        bsr.s   nfoc_rel                release channel block
nc_exit
        movem.l (sp)+,d7/a4/a5          restore regs
        rts
*
nfc_do
nfc_flush
        tst.b   nf_valid(a0)            is block updated?
        bge.s   nfc_close               ... no, just send close
        bsr.s   nf_setwr                ... yes, setup block to write
        bsr.s   nf_sendw                and send (and wait)
        bne.s   nfc_close               ... failed, but try to close
        bsr.l   nf_rstwr                redo pointers
        addq.l  #-err.nc,d0             was it nc?
        beq.s   nfc_flush               not complete yet
*
nfc_close
        moveq   #io.close,d0            set command to close
        bsr.l   nf_setact
        bsr.s   nf_sendw                send and wait
*
nfoc_rel
        bra.l   nd_rechp                release channel block
*
nf_formt
        moveq   #err.iu,d0
        rts
        end
