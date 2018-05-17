* File attributes  V0.8    1984/1985  Tony Tebby  QJUMP
*
*       FLEN (#channel)                 function to find end of file
*       FTYP (#channel)                 function to find file type
*       FDAT (#channel)                 function to find data space required
*       FXTRA (#channel)                function to find extra information
*       FNAME$ (#channel)               function to find filename
*       FUPDT (#channel)                function to find update date
*
        section exten
*
        xdef    flen
        xdef    ftyp
        xdef    fdat
        xdef    fxtra
        xdef    fname$
        xdef    fupdt
* 
        xref    ut_impin                get implicit or channel #n
        xref    ut_fhead
        xref    ut_rtfd1
        xref    ut_rtnam
*
        include dev8_sbsext_ext_keys
*
flen
        moveq   #fh_len,d5              we need length
        bra.s   fh_lword
ftyp
        bsr.s   fheadr                  fetch header
        moveq   #0,d1
        move.b  fh_type(a6,a4.l),d1     set type
        bra.s   fh_retd1
fdat
        moveq   #fh_data,d5             we need data space
        bra.s   fh_lword
fxtra
        moveq   #fh_xtra,d5             we need extra info
        bra.s   fh_lword
fname$
        bsr.s   fheadr                  fetch header
        add.w   #fh_name+1,a4           set address of name (byte character coun
        bra.l   ut_rtnam                return name
fupdt
        moveq   #fh_updt,d5             we need update date
fh_lword
        bsr.s   fheadr                  fetch header
        add.w   d5,a4
        move.l  (a6,a4.l),d1            get appropriate long word
fh_retd1
        bra.l   ut_rtfd1
*
fheadr
        bsr.l   ut_impin                find channel id
        bne.s   fhead_err               ... oops
        moveq   #err.bp,d0              assume error
        cmp.l   a3,a5                   any params left?
        bne.s   fhead_err               ... yes, bad
        move.l  bv_bfbas(a6),a4         set pointer to header
        clr.w   fh_name(a6,a4.l)        clear name length
        clr.l   fh_updt(a6,a4.l)        and update date
        bsr.l   ut_fhead                get file header
        beq.s   fhead_rts
fhead_err
        addq.l  #4,sp                   remove return
fhead_rts
        rts
        end
