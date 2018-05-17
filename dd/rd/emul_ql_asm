; QL RAM disk MDV Emulation   V2.02    1985  Tony Tebby   QJUMP

        section rd

        xdef    rd_emul         emulation check
        xdef    rd_mdv          emulate MDV

        include 'dev8_keys_sys'
        include 'dev8_keys_iod'
        include 'dev8_keys_hdr'
        include 'dev8_keys_err'
        include 'dev8_keys_qdos_sms'
        include 'dev8_dd_rd_data'
        include 'dev8_mac_assert'
rde_mdv  dc.l   'MDVZ'
rde.mdvs equ    254   mdv sectors
rde.maps equ    2     map sectors
rde_offs equ    rdb.slen+rde.maps*rdb.len
;+++
; Checks for RAM disk emulation of other directory drive.
; If so, returns the address of the emulation routine in D0 and sets
; the emulated drive number / drive size (sectors) in D2
; If the RAM disk name is not recognised, it returns ERR.INAM.
;
; ******************************************************************
;
; This version is                                   QL MDV ONLY
;
; ******************************************************************
;
;       d0  r   error status or address of emulation routine
;       d1 c  p length of name
;       d2 cr   0 / preserved or set to drive nr/size
;       a1 c  p pointer to start of name
;
;       status return according to d0
;---
rd_emul
rde.reg reg     d1/d3/a0/a1
        movem.l rde.reg,-(sp)

        cmp.w   #4,d1                    ; could it be 'MDVn'
        blt.s   rde_inam                 ; ... no
        lea     rde_mdv,a0
        moveq   #2,d0
rde_cklp
        moveq   #$ffffffdf,d3
        and.b   (a1)+,d3                 ; mask out lc
        cmp.b   (a0)+,d3                 ; match?
        bne.s   rde_inam                 ; ... no
        dbra    d0,rde_cklp

        moveq   #-'0',d3
        add.b   (a1)+,d3                 ; drive number
        ble.s   rde_inam                 ; ... bad
        cmp.b   #8,d3
        bgt.s   rde_inam                 ; ... bad

        cmp.w   #5,d1                    ; ... just mdv?
        blt.s   rde_set
        bgt.s   rde_inam
        cmp.b   #'_',(a1)                ; must be underscore
        beq.s   rde_set                  ; ... it is
rde_inam
        moveq   #err.inam,d0
        bra.s   rde_exit
rde_set
        moveq   #0,d2
        move.b  d3,d2                    ; emulate drive number
        swap    d2
        move.w  #rde.mdvs+rde.maps,d2    ; and number of sectors
        lea     rd_mdv,a0                ; emulator address
        move.l  a0,d0
rde_exit
        movem.l (sp)+,rde.reg
        rts
        page

sv_tmode equ    $a0     current transmit mode
sv_mdrun equ    $ee     drive currently running

md.read  equ    $124
md.write equ    $126
md.verin equ    $128
md.sectr equ    $12a

md.retry equ    $100    retry
md.bdtry equ    $20     amount taken from retry count every bad medium return
md.rtall equ    $02     retry all sectors twice

md.bad   equ    $ff     bad sector
md.empty equ    $fd     empty sector
md.map   equ    $f8     map sector


pc_tctrl equ    $18002
pc_mctrl equ    $18020
;+++
; Set up MDV emulating RAM disk. The total number of sectors is preset.
; The number of free sectors is set to 2 fewer. The first map sector is set
; to point to itself and it is marked static.
;
;       d1 cr   microdrive number to load /  good sectors
;       d2  r                                total sectors
;       a0 c    base address of fixed RAM disk header
;       a1 c s  base of first map sector
;       a3   s  microdrive control register
;       a4   s
;       a5 c p  pointer to pointer to RAM disk
;---
rd_mdv
        lea     rdb.len(a1),a3          next sector of map
        move.l  a3,rdb_slst(a1)
        addq.l  #1,rdb_sect(a3)         ... it's sector 1
        st      rdb_stat(a1)            (mark as static as well)

        lea     rde_offs(a0),a1         base of normal area
        move.w  #rde.mdvs-1,d0          preset bad sectors

rmd_badloop
        move.b  #md.bad,rdb_id+1(a1)    set true bad
        add.w   #rdb.len,a1
        dbra    d0,rmd_badloop

rmd_wstop
        tst.b   sv_mdrun(a6)            wait until stopped
        bne.s   rmd_wstop

        or.w    #$0700,sr               disable interrupts

        dbne    d0,*                    wait

        move.l  #pc_mctrl,a3
        moveq   #$ffffff00+%11100111,d0 mask out transmit mode
        and.b   sv_tmode(a6),d0         ... from composite mode 
        or.b    #%00010000,d0           set to microdrive mode
        move.b  d0,pc_tctrl-pc_mctrl(a3) ... in peripheral chip
        move.b  d0,sv_tmode(a6)         ... and sysvars

        moveq   #md.rtall,d6            count of retries of all sectors read

        bsr.l   select

; read all sectors in

rmd_retry
        move.l  #pc_mctrl,a3            reset pointer to microdrive control
        bsr.l   read_all                read all sectors

; scrabble through pointers

rmd_sptr
        moveq   #0,d4                   sectors used
        moveq   #0,d5                   highest file number
        moveq   #0,d7                   bad sectors required
        cmp.b   #md.bad,rdb_id+1+rde_offs(a0) bad map?
        beq.s   rmd_bmed                ... yes
        moveq   #-1,d1                  start at file 0

rmd_mfloop
        addq.w  #1,d1                   next file
        cmp.b   #md.map,d1              ... up to map?
        beq.s   rmd_tsts                ... yes
        moveq   #1,d0                   files start at 1 (directory)
        add.w   d1,d0                   ... start link pointer
        asl.w   #2,d0                   ... long words
        lea     rdb.slen+rdb_data(a0,d0.w),a3  in map sector 0
        divu    #rdb.data,d0
        assert  rdb_data,$10
        lsl.w   #4,d0
        add.w   d0,a3                   ... or other sector

        moveq   #-1,d2                  start at sector 0

rmd_msloop
        addq.w  #1,d2                    next sector
        lea     rde_offs(a0),a1          pointer to first sector
        lea     rdb_data(a1),a2          pointer to map
        move.w  #$fe,d3                  limit of map

        moveq   #0,d0
rmd_mploop
        move.b  (a2)+,d0                file number
        cmp.b   (a2)+,d2                block number
        bne.s   rmd_mplend              ... different
        cmp.b   d0,d1                   file number
        beq.s   rmd_mpfound             ... matches
rmd_mplend
        add.w   #rdb.len,a1             next actual sector
        dbra    d3,rmd_mploop
        bra.s   rmd_mfloop

rmd_mpfound
        addq.b  #1,d0                   file+1
        move.w  d1,d5                   highest file found
        cmp.b   rdb_id+1(a1),d0         correct?
        bne.s   rmd_bsect               ... no
        cmp.b   rdb_sect+1(a1),d2       ditto
        beq.s   rmd_lsect               ... yes
rmd_bsect
        addq.w  #1,d7                   one more bad sector
        move.b  #md.bad,rdb_id+1(a1)    set true bad
        tst.w   d6                      last attempt?
        bgt.s   rmd_lsect               ... no
        move.b  d0,rdb_id+1(a1)         ... yes, blotch out the bad sectors
        move.b  d2,rdb_sect+1(a1)
        st      rdb_bad(a1)             and mark invisble bad

rmd_lsect
        addq.w  #1,d4                   one more sector used
        assert  rdb_slst,0
        move.l  a1,(a3)                 ... yes
        move.l  a1,a3
        bra.s   rmd_msloop              ... next sector

; bad medium

rmd_bmed
        subq.w  #1,d6                   one fewer attempts
        bge.l   rmd_retry               try again
        moveq   #err.fmtf,d0            fail, cannot recover
        bra.l   rmd_done

; bad sectors?

rmd_tsts
        tst.w   d7                      any bad sectors?
        beq.s   rmd_free                ... no
        subq.w  #1,d6                   one fewer attempts
        bge.l   rmd_retry

; all done if possible, now create free sector linked list

rmd_free
        assert  rdb_free+rdb.slen,0
        lea     (a0),a3                 free sector linked list
        lea     rde_offs(a0),a1         first data sector pointer
        lea     rdb_data(a1),a2         mdv map
        moveq   #0,d0                   free sectors
        move.w  #rde.mdvs-1,d3          total size (-1)

rmd_frloop
        cmp.b   #md.map,(a2)            map, free or bad?
        blo.s   rmd_frend               ... no
        addq.w  #1,d0                   ... yes, one more free
        move.l  a1,(a3)                 link in
        move.l  a1,a3
rmd_frend
        add.w   #rdb.len,a1             next sector
        addq.w  #2,a2                   next map entry
        dbra    d3,rmd_frloop

        move.w  d0,rdb_fsec+rdb.slen(a0) free sectors
        move.l  #rde.mdvs+rde.maps,d1    set good
        move.l  d1,d2                    and total
        sub.w   d7,d1

;        move.l  d5,d0                   highest file number
;        lsl.l   #6,d0
;        move.l  rdb.slen+rdb_data+4(a0),a1 first directory sector
;        move.l  d0,rdb_data(a1)         set length

; copy headers into directory

        move.l  d5,d4                   starting at last file

rmd_nhdr
        moveq   #1,d3                   internal files start at 1 (directory)
        add.w   d4,d3
        move.l  d3,d0                   ... start link pointer
        asl.w   #2,d0                   ... long words
        lea     rdb.slen+rdb_data(a0,d0.w),a3  in map sector 0
        divu    #rdb.data,d0
        assert  rdb_data,$10
        lsl.w   #4,d0
        move.l  (a3,d0.w),a3            ... or other sector

        move.l  d4,d0
        ror.l   #3,d0                   8 files per dir sector
        move.l  rdb.slen+rdb_data+4(a0),a1 first directory sector
        bra.s   rmd_hdel
rmd_hdlp
        move.l  rdb_slst(a1),a1         next dir sector
rmd_hdel
        dbra    d0,rmd_hdlp

        clr.w   d0
        rol.l   #3,d0                   file within directory
        lsl.w   #6,d0                   position
        lea     rdb_data(a1,d0.w),a1

        cmp.w   d4,d5                   last file?
        bne.s   rmd_lfsect              ... no

        lea     hdr.len(a1),a2
        sub.w   #rdb.data-hdr.len,d0    - amount left in last directory sector
        bge.s   rmd_rmls                ... none

rmd_clrls
        clr.l   (a2)+                   clear rest of last sector
        addq.w  #4,d0
        blt.s   rmd_clrls

rmd_rmls
        lea     -rdb.len(a2),a2         back to start
        move.l  (a2),d0                 next sector
        beq.s   rmd_lfsect              ... none
        clr.l   (a2)                    end of directory

rmd_rmlp
        move.l  d0,a2
        move.l  (a2),d0
        move.l  rdb_free+rdb.slen(a0),(a2)
        move.l  a2,rdb_free+rdb.slen(a0) ... link into free list
        addq.w  #1,rdb_fsec+rdb.slen(a0)

        tst.l   d0                       another?
        bne.s   rmd_rmlp


rmd_lfsect
        move.l  a1,a2
        moveq   #hdr.len/4-1,d0
        cmp.w   rdb_id(a3),d3           the right file?
        bne.s   rmd_clloop              ... no, clear
        tst.b   rdb_bad(a3)             ... yes, bad?
        bne.s   rmd_bfile               ... yes, set bad file
        lea     rdb_data(a3),a3         ... no, copy

rmd_chloop
        move.l  (a3)+,(a2)+             copy header
        dbra    d0,rmd_chloop

        move.w  d3,hdr_flid-hdr.len(a2) and set file ID

; check for bad file

        lea     rdb_slst-rdb_data-hdr.len(a3),a3 check file for bad sectors
rmd_bfloop
        tst.b   rdb_bad-rdb_slst(a3)    bad sector?
        bne.s   rmd_bfile
        move.l  (a3),d0                 another sector?
        beq.s   rmd_xhdr
        move.l  d0,a3
        bra.s   rmd_bfloop              ... no

rmd_bfile
        add.w   #hdr_name,a1            move to name in directory
        move.w  (a1),d0                 length
        addq.w  #1,(a1)+                ... longer
        move.b  #'*',(a1,d0.w)          set blotch in name
        bra.s   rmd_xhdr

rmd_clloop
        clr.l   (a1)+                   clear header
        dbra    d0,rmd_clloop

rmd_xhdr
        subq.w  #1,d4                   previous file
        bgt.l   rmd_nhdr

        moveq   #0,d0                   all ok

; close down drive

retregs reg     d0/d1/d2

rmd_done
        movem.l retregs,-(sp)           save return regs
        move.l  #pc_mctrl,a3            set microdrive control reg value
        bsr.l   deselect

        moveq   #$ffffff00+%11100111,d0 mask out transmit mode
        and.b   sv_tmode(a6),d0         ... from composite mode 
        move.b  d0,pc_tctrl-pc_mctrl(a3) ... in peripheral chip
        move.b  d0,sv_tmode(a6)         ... and sysvars
        and.w   #$f8ff,sr               interrupts enabled
        movem.l (sp)+,retregs           restore return regs

        move.l  d0,d4                   error code
        beq.s   rmd_exit                ... ok

        clr.l   (a5)                    clear pointer to RAM disk
        moveq   #sms.rchp,d0            and return it to heap
        trap    #1
        move.l  d4,d0

rmd_exit
        rts

        page

reglist  reg    d6/a0/a5
sct.head equ    $0e     sector header
stk_rtry equ    $0e     retry word
frame    equ    $10 
stk_d6   equ    $10
stk_a0   equ    $14
stk_a5   equ    $18

read_all
        movem.l reglist,-(sp)
        moveq   #-1,d6                  first sector read
        dbra    d6,*                    and pause
        move.w  d6,(sp)                 smash top half of call d6
        sub.w   #frame,sp               room for sector header and retry

rmr_nsect
        move.w  #md.retry+md.bdtry,stk_rtry(sp) do not carry on for ever

rmr_bsect
        sub.w   #md.bdtry,stk_rtry(sp)  bad medium ... not so many retries
rmr_rsect
        subq.w  #1,stk_rtry(sp)
        blt.s   rmr_exit                ... tried too many times
        move.w  md.sectr,a2             read sector header
        move.l  sp,a1                   onto stack
        jsr     $4000(a2)
        bra.s   rmr_bsect               ... bad medium
        bra.s   rmr_rsect               ... not a sector header, read next

        and.w   #$00ff,d7               make sector number a word
        move.w  stk_d6(sp),d6           first sector
        bpl.s   rmr_ckend               ... already set
        move.w  d7,stk_d6(sp)           ... no, set it
rmr_ckend
        cmp.w   d7,d6                   all sectors read?
        beq.s   rmr_exit                ... done
rmr_saddr
        mulu    #rdb.len,d7
        move.l  stk_a0(sp),a0
        lea     rdb_data+rde_offs(a0),a1
        add.l   d7,a1                   pointer to sector in RAM disk
        cmp.b   #md.bad,rdb_id+1-rdb_data(a1) already read?
        bne.s   rmr_nsect               ... yes, do next sector
        move.w  md.read,a2              ... no, read
        jsr     $4000(a2)
        bra.s   rmr_nsect               ... oops, try next sector
        addq.w  #1,d1
        move.b  d1,rdb_id+1-rdb.len(a1)   set file
        move.b  d2,rdb_sect+1-rdb.len(a1) ... and block
        bra.s   rmr_nsect               read next sector

rmr_exit
        add.w   #frame,sp
        movem.l (sp)+,reglist
        rts

; select and deselect

select
        moveq   #%11,d2                clock high / select bit set
        subq.w  #1,d1
        bra.s   sel_loop
deselect
        moveq   #%10,d2                 clock high / select bit not set
        moveq   #7,d1
sel_loop
        move.b  d2,(a3)                 clock high
        moveq   #32,d0                  wait
        ror.l   d0,d0                   ... 70 cycles (10 us)
        ror.l   d0,d0                   ... 70 cycles (10 us)
        ror.l   d0,d0                   ... 70 cycles (10 us)
        ror.l   d0,d0                   ... 70 cycles (10 us)
        and.b   #%01,d2                 clock low
        move.b  d2,(a3)
        moveq   #32,d0                  wait
        ror.l   d0,d0                   ... 70 cycles (10 us)
        ror.l   d0,d0                   ... 70 cycles (10 us)
        ror.l   d0,d0                   ... 70 cycles (10 us)
        ror.l   d0,d0                   ... 70 cycles (10 us)
        moveq   #%10,d2                 clock high again
        dbra    d1,sel_loop
        rts
        end
