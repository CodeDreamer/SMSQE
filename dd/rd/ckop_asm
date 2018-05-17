; Check RAM disk for open     V2.02     1989   Tony Tebby   QJUMP

        section rd

        xdef    rd_ckop

        xref    rd_alsec
        xref    rd_alroot
        xref    rd_remv

        include 'dev8_keys_iod'
        include 'dev8_keys_hdr'
        include 'dev8_keys_err'
        include 'dev8_dd_rd_data'

;+++
; This routine sets up the standard IOD physical definition. This might
; have been cleared by a DEL_DEFB. If there is no RAM disk, the first
; sector of the map and the first sector of the root directory are allocated.
;
;       d1   s
;       d3   s
;       d6 c  p drive number / (zero)
;       a0 c  p pointer to channel block
;       a1   s
;       a2   s
;       a3 c  p pointer to linkage block
;       a4 c  p pointer to physical definition
;       a5 cr   pointer to map
;
;       status return 0
;---
rd_ckop
        move.l  d6,d1
        swap    d1
        lsl.w   #2,d1                    ; index drive table in linkage
        lea     rdd_driv(a3),a2
        add.w   d1,a2
        move.l  (a2),d0                  ; address of RAM disk
        move.l  d0,a5
        bne.s   rck_reset                ; set up again
        jsr     rd_alsec                 ; allocate first sector of map
        bne.s   rck_exit
        move.l  d0,(a2)                  ; and point to it
        move.l  d0,a5
        bsr.s   rck_set
        move.l  a2,rdb_data(a2)          ; set pointer to map itself

        jsr     rd_alroot                ; allocate first sector of root dir
        bne.s   rck_rmap                 ; ... oops, remove map
        move.l  #hdr.len,iod_rdln(a4)    ; set root directory length
        bra.s   rck_ok

rck_rmap
        moveq   #0,d1
        move.b  iod_dnum(a4),d1          ; set drive number
        jsr     rd_remv                  ; remove drive
        moveq   #err.imem,d0
        bra.s   rck_exit

rck_reset
        tst.l   rdd_map(a4)              ; address of map
        bne.s   rck_ok                   ; ... set already
rck_set
        move.w  #1,iod_rdid(a4)          ; root directory ID
        move.l  #hdr.len,iod_hdrl(a4)    ; set header
        move.w  #rdb.data,iod_allc(a4)   ; allocation unit
        move.b  #iod.qlwa,iod_ftyp(a4)   ; set type
        lea     iod_mnam(a4),a2
        move.l  #'RAM0',(a2)+
        swap    d6
        add.b   d6,-1(a2)                ; with number
        swap    d6
        move.l  #'    ',(a2)+            ; padded
        move.w  #'  ',(a2)+

        move.l  d0,a2
        move.l  rdb_data+4(a2),d0        ; address of file 1
        beq.s   rck_ok
rck_acdl
        add.l   #rdb.data,iod_rdln(a4)   ; accumulate root directory length
        move.l  d0,a2
        move.l  rdb_slst(a2),d0          ; next sector
        bne.s   rck_acdl                 ; there is one

rck_ok
        moveq   #0,d0
rck_exit
        rts
        end
