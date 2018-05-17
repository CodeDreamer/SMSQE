* Read file header   V0.0    1985   Tony Tebby   QJUMP
*
        section utils
*
        xdef    ut_fhead                fetch file header into BASIC buffer
        xdef    ut_fdire                fetch directory entry (d0 = fstrg)
*
        xref    ut_trp3r
*
        include dev8_sbsext_ext_keys
*
ut_fhead
        moveq   #fs.headr,d0            header read
ut_fdire
        moveq   #fs.hdlen,d2            ... full length
        move.l  bv_bfbas(a6),a1         into BASIC buffer
        bsr.l   ut_trp3r
        move.l  bv_bfbas(a6),a1         reset BUFFER pointer
        rts
        end
