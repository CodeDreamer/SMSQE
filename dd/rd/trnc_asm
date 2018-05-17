; RAM disk driver truncate file/disk   V2.02    1989  Tony Tebby   QJUMP

        section rd

        xdef    rd_trnc         truncate file
        xdef    rd_remv         remove ram disk

        xref    gu_rchp

        include 'dev8_keys_chn'
        include 'dev8_dd_rd_data'
        include 'dev8_mac_assert'
;+++
;
;       d0  r   0
;       d2   s
;       d6 c  p drive number (word in msw)
;       a2   s  pointer to buffer
;       a3 c  p pointer to linkage block
;       a5   s  pointer to pointer to map
;
;       all other registers preserved
;---
rd_remv
        move.l  a0,-(sp)                 ; save a0
        move.l  d6,d2
        swap    d2
        lsl.w   #2,d2                    ; index map table
        lea     rdd_driv(a3),a5
        add.w   d2,a5
        move.l  a5,-(sp)                 ; save it
        move.l  (a5),d0                  ; contents
        beq.s   rdr_exit                 ; ... nothing
        clr.l   (a5)                     ; ... at least not now
        move.l  d0,a5
        tst.b   rdb_stat(a5)             ; static?
        beq.s   rdr_rema                 ; ... no
        lea     -rdb.slen(a5),a0         ; ... yes, remove all at once
        jsr     gu_rchp
        bra.s   rdr_exit

; loop through, removing all files

rdr_rema
        moveq   #4,d2                    ; skip the map sector itself
rdr_remf
rdr_floop
        move.l  rdb_data(a5,d2.w),d0     ; file in this sector
        beq.s   rdr_nfile
        move.l  d0,a2
        bsr.s   rdt_rdyn                 ; remove all sectors
rdr_nfile
        addq.w  #4,d2
        cmp.w   #rdb.data,d2
        blt.s   rdr_floop

        move.l  a5,a0
        jsr     gu_rchp                  ; return map sector
        moveq   #0,d2                    ; restart at beginning
        assert  rdb_slst,0
        move.l  (a5),a5                  ; next map sector
        move.l  a5,d0
        bne.s   rdr_remf                 ; ... there is one

rdr_exit
        move.l  (sp)+,a5                 ; set pointer to pointer to map
        move.l  (sp)+,a0                 ; restore a0
        rts
;+++
; Truncate file
;
;       d0  r   0
;       d3  r   index to drive table
;       d4   s  block
;       d5 c  p file pointer
;       d6 c  p drive number / file ID
;       a0 c  p channel definition block
;       a2   s  pointer to buffer
;       a4 c  p pointer to drive definition block
;       a5 c  p pointer to map
;
;       all other registers preserved
;---
rd_trnc
        clr.l   chn_csb(a0)              ; clear pointer
        move.l  a0,-(sp)                 ; save a0
        move.l  d5,d4                    ; all files?
        ble.s   rdt_map
        subq.l  #1,d4
        lsl.l   #rdb.sft,d4              ; block in msw
        swap    d4                       ; ... lsw
rdt_map
        addq.l  #1,d4
        move.l  a5,a2
        moveq   #0,d0
        move.w  d6,d0                    ; file ID
rdt_ffsc
        sub.w   #rdb.nfls,d0             ; in this sector?
        blo.s   rdt_fget                 ; ... yes
        assert  rdb_slst,0
        move.l  (a2),a2                  ; next sector
        bra.s   rdt_ffsc
rdt_fget
        add.w   #rdb.nfls,d0
        lsl.l   #2,d0
        assert  rdb_slst,0
        lea     rdb_data(a2,d0.l),a2     ; address of first sector pointer
rdt_lcsl
        move.l  a2,a0
        move.l  (a0),a2                  ; next sector
        move.l  a2,d0
        beq.s   rdt_exit                 ; ... off the end
        cmp.w   rdb_sect-rdb_slst(a2),d4 ; the right sector?
        bhi.s   rdt_lcsl                 ; ... not yet

rdt_sfound
        clr.l   (a0)                     ; clear pointer to this sector

        bsr.s   rdt_remv                 ; release sectors

rdt_exit
        move.l  (sp)+,a0                 ; restore channel base
        rts
        page

; truncate all sectors from here (d0)

rdt_remv
        tst.b   rdb_stat(a5)             ; static?
        bne.s   rdt_rstt                 ; ... yes
rdt_rdyn
        assert  rdb_slst,0
        move.l  d0,a0
        jsr     gu_rchp                  ; return sector
        move.l  d0,a2
        move.l  (a2),d0                  ; ... and the next
        bne.s   rdt_rdyn
        rts

; release a sector in static ram disk

rdt_rstt
        addq.w  #1,rdb_fsec(a5)          ; one more free sectors
        assert  rdb_slst,0
        move.l  d0,a2
        move.l  (a2),d0                  ; the next sector
        move.l  rdb_free(a5),(a2)
        move.l  a2,rdb_free(a5)          ; link free
        tst.l   d0
        bne.s   rdt_rstt                 ; ... and again
        rts
        end
