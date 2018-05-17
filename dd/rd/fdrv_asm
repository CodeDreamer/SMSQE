; RAM disk driver FORMAT   V2.02    1989  Tony Tebby   QJUMP

        section rd

        xdef    rd_fdrv          ; format drive

        xref    rd_remv          ; remove ram disk
        xref    rd_emul          ; check device emulation
        xref    rd_alfs          ; allocate first sector
        xref    gu_achpp

        include 'dev8_keys_sys'
        include 'dev8_keys_iod'
        include 'dev8_keys_err'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_qlv'
        include 'dev8_dd_rd_data'
;+++
; Format RAM disk - first it removes old RAM disk
;
;       d0  r   status
;       d1  r   drive number / good sectors
;       d2  r   total sectors = d1
;       d6 c s  msw drive number
;       a1 c s  medium name
;       a3 c  p linkage block
;       d3-d7/a0/a2/a4/a5 scratch
;
;       status return standard
;---
rd_fdrv
        bsr.l   rd_remv                  ; remove old RAM disk

rdf_name
        moveq   #0,d2                    ; ... no sectors
        moveq   #0,d1
        move.w  (a1)+,d1                 ; get length of name
        subq.w  #5,d1                    ; less RAMn_
        ble.s   rdf_ok
        addq.l  #5,a1
        jsr     rd_emul                  ; check for emulation
        move.l  d0,a4                    ; save emulation address
        bgt.s   rdf_alloc                ; ... it is

rdf_num
        move.l  a1,a0                    ; set start of number
        move.l  a0,d7
        add.l   d1,d7                    ; set end of number
        move.l  sp,a1                    ; put length on stack
        subq.l  #2,sp
        move.l  a6,-(sp)                 ; and sysvar
        sub.l   a6,a6
        move.w  cv.deciw,a2              ; convert integer
        jsr     (a2)
        move.l  (sp)+,a6
        move.w  (sp)+,d2
        cmp.l   a0,d7                    ; all of name converted?
        bne.s   rdf_inam
        tst.l   d0                       ; conversion correct?
        bne.s   rdf_inam

rdf_alloc
        move.l  d2,rdd_emul-rdd_driv(a5) ; set size
        move.w  d2,d0
        beq.s   rdf_ok
        cmp.w   #3,d0                    ; at least 3?
        blt.s   rdf_inam
        mulu    #rdb.len,d0              ; room for linkage+sectors
        addq.l  #rdb.slen,d0             ; plus room for fixed header
        jsr     gu_achpp
        bne.s   rdf_exit

        lea     rdb_fsec-rdb_free(a0),a1 ; set static header
        move.w  d2,(a1)                  ; free sectors
        subq.w  #2,(a1)+                 ; ... not first map / dir sectors
        move.w  d2,(a1)+                 ; total sectors

        move.l  a1,(a5)                  ; set ram address
        st      rdb_stat(a1)             ; say preallocated
        move.l  a1,rdb_data(a1)          ; point to map itself

        move.l  a4,d0                    ; emulation?
        bgt.s   rdf_emul                 ; ... yes

        move.w  d2,d0                    ; number of sectors
        subq.w  #2,d0
        add.w   #rdb.len,a1              ; start of directory
        move.l  a1,rdb_data+4-rdb.len(a1) ; set pointer to first sector

rdf_psloop
        add.w   #rdb.len,a1              ; next sector
        move.l  a1,(a0)                  ; preset link
        move.l  a1,a0
rdf_pslend
        subq.w  #1,d0
        bgt.s   rdf_psloop

rdf_ok
        move.w  d2,d1                    ; set sector count
        moveq   #0,d0
rdf_exit
        rts
rdf_inam
        moveq   #err.inam,d0
        rts
rdf_emul
        moveq   #0,d1
        move.w  rdd_emul-rdd_driv(a5),d1 ; drive number
        jmp     (a4)                     ; do emulation setup
        end
