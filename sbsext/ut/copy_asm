* Copy one file to another  V0.2    1985   Tony Tebby   QJUMP
*
        section utils
*
        xdef    ut_copy1                copy header if type>=1 and copy file
        xdef    ut_copy                 copy header? and file
        xdef    ut_copys                copy subroutine does not close files
*
        xref    ut_fclos                file close
        xref    ut_eofok                set eof ok
        xref    ut_ok                   return ok
        xref    ut_fhead                read file header
        xref    ut_wrtch                write a character
        xref    ut_wrtst                write string of bytes
        xref    ut_trp3r                trap #4, trap #3 d3=-1
*
        include 'dev8_keys_qdos_io'
        include 'dev8_sbsext_ext_keys'
*
*       d5 c    lsb: 1 copy header if type >=1, 0 with, 255 without header
*       a0 c    destination channel ID
*       a2   s  length of file
*       a3 c    source channel ID
*
ut_copy1
        moveq   #1,d5                   copy with header if type 1 or higher
ut_copy
        bsr.s   ut_copys
        bsr.s   utc_close               close destination
        move.l  a3,a0
utc_close
        bra.s   ut_fclos                close other file
*
ut_copys
        move.w  #-1,a2                  very long file
        tst.b   d5                      is header to be copied?
        blt.s   ut_copyf                ... just copy file
        exg     a0,a3                   get source channel ID
        bsr.s   ut_fhead                read header
        exg     a0,a3
        bne.s   ut_copyf                ... no header, just carry on and copy fi
        move.l  fh_len(a6,a1.l),a2      ... length of file
        cmp.b   fh_type(a6,a1.l),d5     copy this type of header?
        bhi.s   ut_copyf                ... no, just carry on and set the versio
        moveq   #fs.heads,d0            set header
        bsr.l   ut_trp3r                (don't care about errors)
*
ut_copyf
        exg     a0,a3
        moveq   #iof.vers,d0            get version
        moveq   #iofd.get,d1
        trap    #do.io
        exg     a0,a3
        tst.l   d0
        bne.s   utc_loop
        moveq   #iof.vers,d0            set version
        trap    #do.io

utc_loop
        moveq   #$40,d1
        add.l   d1,d1                   get up to $80 bytes only
        move.l  a2,d2                   get remaining number of bytes
        beq.s   ut_ok                   ... done
        cmp.l   d1,d2                   more than $80 left?
        bls.s   utc_fetch               ... no
        move.l  d1,d2                   ... yes, just get $80
utc_fetch
        moveq   #io.fstrg,d0            fetch string of bytes
        moveq   #5,d3                   wait 1/10 a second
        move.l  bv_bfbas(a6),a1         set buffer pointer
        exg     a3,a0                   source channel ID
        trap    #4
        trap    #3                      do read
        exg     a0,a3
        move.l  d0,d4                   save error flag
        sub.w   d1,a2                   decrement number of bytes to copy
        move.w  d1,d2                   set number of bytes to write
        beq.s   utc_test                ... none
        move.l  bv_bfbas(a6),a1         set buffer pointer
        bsr.l   ut_wrtst                and write string of bytes
        bne.s   ut_eofok                ... oops
utc_test
        move.l  d4,d0                   test the return from read
        beq.s   utc_loop
        tst.b   bv_brk(a6)              check for break
        beq.s   ut_eofok                ... broken
        addq.l  #-err.nc,d4             not complete is quite normal
        beq.s   utc_loop
        bra.s   ut_eofok
        end
