* Network remote file  utils  V0.1    1985   Tony Tebby   QJUMP
*
        section nd
*
        xdef    nf_chkrd                check buffer for read
        xdef    nf_chkwr                check buffer for write
*
        xdef    nf_wblok                write a block
        xdef    nf_send                 send a packet
        xdef    nf_sendw                send a packet and wait
        xdef    nf_setwr                set up to write a block
        xdef    nf_rstwr                reset the pointers after writing
        xdef    nf_setky                set key and ID in packet
        xdef    nf_setact               set key, ID, d1 and d2
        xdef    nf_setln                set length of packet
*
        xref    nf_eof                  set end of file
*
        xref    nd_rept                 do repeated op
        xref    nd_read0                read 0 block
        xref    nd_send0                send 0 block
*
        include dev8_dd_qlnd_keys
*
* send a packet and get reply (waiting)
*
nf_sendw
        lea     nd_send0(pc),a2         set address of send
        bsr.l   nd_rept                 and try repeatedly
        bne.s   nfsp_rts                ... oops
nf_readw
        lea     nd_read0(pc),a2         set address of read
        bsr.l   nd_rept                 and try repeatedly
        bne.s   nfsp_rts
        lea     nd_data(a0),a5          set address of block
nfsp_rts
        rts
*
* Setup to write block
*
nf_setwr
        moveq   #nf.sblk,d0             send block key
        bsr.s   nf_setky                set key and ID
        move.l  nf_bfbot(a0),d0         bottom file address in buffer
        move.l  d0,(a5)+
        move.l  nf_bffil(a0),d5         address filled to
        sub.l   d0,d5                   amount to send
        move.l  d5,(a5)+
        moveq   #$f,d0                  round up to multiple of four bytes
        add.l   d5,d0                   ... and include 3 long word preamble
        lsr.w   #2,d0
        move.b  d0,nd_nbyt(a0)          set length to send (/4)
        rts
*
* Write block (if modified), OK return is 0 or +ve
*
nf_wblok
        tst.b   nf_valid(a0)            is block modified?
        bge.s   nfw_rts                 ... no
        bsr.s   nf_setwr                set up to write
        bsr.s   nf_send                 send packet to read or write block
        bne.s   nfw_rts
*
* Reset pointers after successful write packet (done is + or zero)
*
nf_rstwr
        move.l  (a5)+,d0                get error code
        beq.l   nfc_valid               ... done
        moveq   #err.nc,d7              we can only adjust if nc
        cmp.l   d7,d0
        blt.s   nfw_rts                 ... oops
        move.l  (a5)+,d7                get number of bytes sent
        add.l   d7,nf_bfbot(a0)         adjust the pointers
        add.l   d7,nf_bftop(a0)
*
        sub.w   d7,d5                   number of bytes not sent
        addq.l  #4,a5                   skip third long word of data block
nf_adjloop
        move.b  (a5,d7.l),(a5)+         move byte down
        subq.w  #1,d5
        bgt.s   nf_adjloop
        tst.l   d0                      set error code
nfw_rts
        rts
*
* Send packet and wait for reply
*
nf_send
        bsr.l   nd_send0                and send block 0
        tst.l   d0                      (+ve could be not complete too!!)
        blt.s   nfw_rts                 ... oops
        bra.s   nf_readw                wait for response
        page
*
* Setup key (and channel ID)
*
nf_setky
        move.b  d0,nd_type(a0)          set type of transaction
        lea     nd_data(a0),a5          address of data block
        move.l  a5,a2                   save the address
        move.l  nf_chid(a0),(a5)+       set channel ID
        rts
*
* Set action only packet
*
nf_setact
        bsr.s   nf_setky                set key and ID
        move.l  d1,(a5)+                set d1,d2
        move.l  d2,(a5)+
*
nf_setln
        lea     nd_data(a0),a2          reset base of data block
        move.l  a5,d5                   find the length of the data block
        sub.l   a2,d5
        move.b  d5,nd_nbyt(a0)          and set it
        rts
        page
*
* Check if current block is the required one, OK return is 0 or +ve
*
nf_chkrd
        move.l  nf_bfpnt(a0),d6         get current pointer
        tst.b   nf_eoff(a0)             eof set?
        beq.s   nf_ckrd1                ... no
        cmp.l   nf_eofpt(a0),d6         ... off eof?
        bge.l   nf_eof                  ... ... yes
nf_ckrd1
        move.l  nf_bffil(a0),d5         for read, check against fill address
        tst.b   nf_sonly(a0)            is it serial only?
        beq.s   nf_chkbf                ... no, check the block
        tst.b   nf_valid(a0)            is it modified?
        bge.s   nf_chkfl                ... no, check if pointer off buffer
        bsr.s   nf_wblk1                ... yes, write buffer
        blt.s   nfc_rts1
        bra.s   nf_rblok                and read next one
*
nf_chkfl
        cmp.l   d5,d6                   out of buffer?
        blt.s   nfc_ok1                 ... no, ok
        bra.s   nf_rblok                ... yes, read next block
*
nf_chkwr
        move.l  nf_bftop(a0),d5         for write, check against top address
        move.l  nf_bfpnt(a0),d6         set current pointer
        tst.b   nf_sonly(a0)            is it serial only?
        beq.s   nf_chkbf                ... no
        tst.b   nf_valid(a0)            ... yes, is it part full?
        blt.s   nf_chksw                ... yes, check room
        lea     nf_bfbot(a0),a5         and reset pointers
        clr.l   (a5)+
        clr.l   (a5)+
        move.l  #$100,(a5)+             short buffer only for serial
        clr.l   (a5)
        rts                             returns zero
*
nf_chksw
        cmp.l   d5,d6                   room in buffer?
        blt.s   nfc_ok1                 ... yes
nf_wblk1
        bra.l   nf_wblok                write out buffer
*
nf_chkbf
        tst.b   nf_valid(a0)            is buffer valid?
        beq.s   nf_rblok                ... no, read buffer
        cmp.l   nf_bfbot(a0),d6         is pointer within buffer?
        blt.s   nfc_gblok               ... off bottom
        cmp.l   d5,d6                   off top?
nfc_ok1
        blt.s   nfc_ok                  ... no, ok
*
nfc_gblok
        bsr.s   nf_wblk1                write block (if modified)
nfc_rts1
        blt.s   nfc_rts                 ... oops
*
nf_rblok
        sf      nf_valid(a0)            say block invalid in case transfer fails
        moveq   #$ffffff80,d7
        lsl.w   #2,d7                   d7 is 512 byte block mask
        moveq   #$40,d0                 d0 is header offset
        move.l  d7,d5
        neg.l   d5                      d5 is block length
        add.l   d0,d6
        and.l   d6,d7                   d7 is start of block + header
        sub.l   d0,d7                   d7 is start of block
        bgt.s   nf_rb_do                ... ok
        moveq   #0,d7                   start of file
        sub.l   d0,d5                   read part block only
*
nf_rb_do
        moveq   #nf.rblk,d0             set read block key
        bsr.l   nf_setky                set key
        move.l  d7,(a5)+                set block required
        move.l  d5,(a5)+                set length of block
        move.l  d7,nf_bfbot(a0)         set buffer pointers
        move.l  d7,nf_bffil(a0)
        add.l   d5,d7
        move.l  d7,nf_bftop(a0)
        bsr.l   nf_setln                set length of packet
        bsr.l   nf_send                 send packet and wait for reply
        bne.s   nfc_rts                 ... oops
*
        move.l  (a5)+,d0                get error code
        move.l  (a5)+,d5                add number of bytes read
        add.l   d5,nf_bffil(a0)         to fill address
        moveq   #err.nc,d7              check for not complete or OK
        cmp.l   d7,d0
        bge.s   nfc_sset                ... yes, reset pointer if serial only
        moveq   #err.ef,d7              check for end of file
        cmp.l   d7,d0
        bne.s   nfc_rts                 ... no
        st      nf_eoff(a0)             set eof flag
        move.l  nf_bffil(a0),nf_eofpt(a0) ... and eof pointer
nfc_sset
        tst.b   nf_sonly(a0)            is it serial only?
        beq.s   nfc_valid               ... no
        move.l  nf_bfbot(a0),nf_bfpnt(a0) .. yes, reset pointer to base of buffer
nfc_valid
        move.b  #1,nf_valid(a0)         say contents valid
nfc_ok
        moveq   #0,d0                   done
nfc_rts
        rts
        end
