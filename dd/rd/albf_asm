; RAM disk driver allocate / locate buffer   V2.00    1985  Tony Tebby   QJUMP

        section rd

        xdef    rd_lcbf          ; locate buffer
        xdef    rd_albf          ; locate / allocate buffer
        xdef    rd_alfs          ; allocate first sector
        xdef    rd_alroot        ; allocate first sector of root
        xdef    rd_alsec

        xref    gu_achpp

        include 'dev8_keys_err'
        include 'dev8_keys_chn'
        include 'dev8_dd_rd_data'
        include 'dev8_mac_assert'
;+++
;       d0  r   error return
;       d3  r   number of bytes remaining in buffer
;       d4  r   block / byte
;       d5 c  p file pointer
;       d6 c  p drive number / file ID
;       a0 c  p channel definition block
;       a2  r   pointer to buffer
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to first map sector
;
;       all other registers preserved
;---
rd_lcbf
;+++
;       d0  r   error return
;       d3  r   number of bytes remaining in buffer
;       d4  r   block / byte
;       d5 c  p file pointer
;       d6 c  p drive number / file ID
;       a0 c  p channel definition block
;       a2  r   pointer to buffer
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to first map sector
;
;       all other registers preserved
;---
rd_albf
        move.l  d5,d4
        lsl.l   #rdb.sft,d4
        lsr.w   #rdb.sft,d4              ; block / byte
        swap    d4

        move.l  chn_csb(a0),d0           ; point to current block
        beq.s   rda_rsec                 ; ... none
        move.l  d0,a2
        cmp.w   rdb_id-rdb_slst(a2),d6   ; correct file? (could be directory)
        bne.s   rda_rsec                 ; ... no
        cmp.w   rdb_sect-rdb_slst(a2),d4 ; correct sector
        beq.s   rda_ssect                ; ... yes
        bhi.s   rda_nsec                 ; ... no, locate the correct sector

rda_rsec
        move.l  a5,a2                    ; first sector of map
        moveq   #0,d0
        move.w  d6,d0                    ; file ID
rda_ffsc
        sub.w   #rdb.nfls,d0             ; in this sector?
        blo.s   rda_fget                 ; ... yes
        assert  rdb_slst,0
        move.l  (a2),a2                  ; next sector
        bra.s   rda_ffsc
rda_fget
        add.w   #rdb.nfls,d0
        lsl.l   #2,d0
        move.l  rdb_data(a2,d0.l),d0     ; address of first sector

rda_lcs
        move.l  d0,a2
        cmp.w   rdb_sect-rdb_slst(a2),d4 ; the right sector
        beq.s   rda_sfound               ; ... yes
rda_nsec
        assert  rdb_slst,0
        move.l  (a2),d0                  ; next sector
        bne.s   rda_lcs

        bsr.s   rda_alloc                ; allocate new sector
        bne.s   rda_exit
        move.l  d0,(a2)                  ; set link
        move.l  d0,a2

        move.w  d6,rdb_id-rdb_slst(a2)   ; set id
        move.w  d4,rdb_sect-rdb_slst(a2) ; and sector

rda_sfound
        move.l  a2,chn_csb(a0)           ; set current block pointer
rda_ssect
        swap    d4                       ; back to block / byte
        lea     rdb_data-rdb_slst(a2,d4.w),a2 ; set pointer
        move.l  #rdb.data,d3
        sub.w   d4,d3                    ; amount left in block
        moveq   #0,d0
rda_exit
        rts
        page
;+++
; Allocate or re-allocate first sector of file
;       d0  r   error return
;       d1 cr   old file ID / new file ID
;       d6 c  p drive number / ???
;       a0 c  p channel definition block
;       a2  r   pointer to buffer
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to first map sector
;
;       all other registers preserved
;---
rd_alfs
        tst.w   d1                       ; re-allocate?
        bne.s   rdfs_ok
;+++
; Allocate first sector of root
;
;       d0  r   error return
;       d1  r   new file ID
;       d6 c  p drive number / ???
;       a0 c  p channel definition block
;       a2  r   pointer to buffer
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to first map sector
;
;       all other registers preserved
;---
rd_alroot
        move.l  a5,d0
        moveq   #0,d1                    ; file ID
rdfs_look
        move.l  d0,a2
        move.w  #rdb.nfls,d0
        add.w   d0,d1
        subq.w  #1,d0
        lea     rdb_data(a2),a2
rdfs_lz
        tst.l   (a2)+                    ; empty hole?
        dbeq    d0,rdfs_lz
        beq.s   rdfs_sadd                ; ... yes

        move.l  rdb_slst-rdb.len(a2),d0  ; next sector
        bne.s   rdfs_look

        bsr.s   rda_alloc                ; ... no more, allocate a new one
        bne.s   rdfs_exit
        move.l  d0,rdb_slst-rdb.len(a2)  ; link in
        move.l  d0,a2
        moveq   #-1,d0
        lea     rdb_data+4(a2),a2        ; one past next hole in list

rdfs_sadd
        sub.w   d0,d1
        subq.w  #1,d1                    ; file ID
        subq.l  #4,a2                    ; address

        bsr.s   rda_alloc                ; new sector
        bne.s   rdfs_exit
        move.l  d0,(a2)                  ; set address
        move.l  d0,a2
        move.w  d1,rdb_id(a2)            ; and ID
rdfs_ok
        moveq   #0,d0
rdfs_exit
        rts
        page
rda_alloc
        tst.b   rdb_stat(a5)             ; static RAM disk
        bne.s   rda_alfix                ; ... allocate in fixed area
;+++
; Allocate a new sector in heap
;
;       d0  r   error or sector address
;       all other registers preserved
;
;       status return standard
;---
rd_alsec
        move.l  a0,-(sp)
        move.l  #rdb.len,d0
        jsr     gu_achpp                 ; allocate
        bne.s   rdas_drfl                ; drive full
        exg     a0,d0                    ; return sector address
        bra.s   rdas_exit

rda_alfix
        move.l  a0,-(sp)
        move.l  rdb_free(a5),d0          ; all gone?
        beq.s   rdas_drfl                ; ... yes
        move.l  d0,a0                    ; ... no, set pointer to next
        move.l  (a0),rdb_free(a5)        ; link past
        subq.w  #1,rdb_fsec(a5)          ; one fewer free
        clr.l   (a0)+                    ; clear out header
        clr.l   (a0)+
        clr.l   (a0)+
        clr.l   (a0)+
rdas_exit
        move.l  (sp)+,a0
        rts

rdas_drfl
        moveq   #err.drfl,d0
        bra.s   rdas_exit
        end
