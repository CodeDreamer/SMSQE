; Add Microdrive Information to File List  V1.00   1989  Tony Tebby  QJUMP
;                                       after Jonathan Oakley
        section gen_util

        xdef    gu_mdvfl

        xref    gu_prlis

        include 'dev8_keys_hdr'
        include 'dev8_keys_qlv'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_qdos_ioa'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_fll'


; Come here to check that the drive whose directory we've read is not an
; MDV: if it is we need to open every matched file and read its header,
; so we have complete information on the files.

gu_mdvfl
mdvreg   reg    d5/a0/a5
mdstk_d5 equ    $00    
        movem.l mdvreg,-(sp)
        move.l  a1,a5                   ; keep list safe
        move.l  a0,d3                   ; from channel ID
        add.w   d3,d3
        add.w   d3,d3                   ; make offset of channel base in table
        moveq   #sms.info,d0
        trap    #do.sms2                ; point to system variables
        move.l  sys_chtb(a0),a1         ; and to channel table
        move.l  0(a1,d3.w),a1           ; and to channel base

        move.l  chn_drvr(a1),a2         ; point to driver address
        cmp.l   #$c000,iod_frmt(a2)     ; is FORMAT entry in ROM?
        bge.s   fld_done                ; no, can't be mdv then
        move.w  iod_dnus(a2),d2         ; length of drive name
        addq.w  #2,d2                   ; plus number and underscore

        moveq   #sys_fsdd>>2,d0
        add.b   chn_drid(a1),d0         ; get drive ID
        lsl.b   #2,d0                   ; offset in pdb table
        move.l  0(a0,d0.w),a2           ; point to drive's pdb

;       We now have A2 pointing to a microdrive's physical definition block.
;       Assuming that we're 13 sectors on from the last directory sector, we
;       can search from there to each file's block 0 in turn, and read the
;       headers in the order they're found on the medium.

        lea     md_maps+md.mapl(a2),a1  ; point to end of map
        move.w  #256,d4                 ; starting at block 255
        move.w  #$ff00,d6               ; look for duff sectors
fld_fszl
        subq.w  #1,d4                   ; this sector
        cmp.w   -(a1),d6                ; is it duff
        beq.s   fld_fszl                ; yes, try next
        moveq   #-1,d0                  ; looking for directory sector>=0
        move.w  d4,d3                   ; starting here

fld_fdrl
        tst.b   (a1)                    ; is this a directory block? 
        bne.s   fld_fdre                ; no
        cmp.b   1(a1),d0                ; yes, highest so far?
        bgt.s   fld_fdre                ; no
        move.w  d3,d6                   ; yes, update position
        move.b  1(a1),d0                ; this is highest now
fld_fdre
        subq.l  #2,a1                   ; next map entry
        dbra    d3,fld_fdrl             ; loop until done

        add.w   d4,d4                   ; max index in map
        add.w   d6,d6                   ; start point in map
        sub.w   #(13+1)*2,d6            ; move back 13 blocks
        bpl.s   fld_ffnl                ; OK, find first file number
        add.w   d4,d6                   ; past start, so back to here

; At this point, A2^map, D4=max map index, D6=start map index

fld_ffnl
        moveq   #0,d1
        move.b  md_maps(a2,d6.w),d1     ; get file number
        cmp.b   #$f8,d1                 ; is it a file?
        bcc.s   fld_nofl                ; no, do next
        tst.b   md_maps+1(a2,d6.w)      ; yes, is it that file's block 0?
        bne.s   fld_nofl                ; no, no point reading it then

; We've found a file number which we think we can open quickly, so now
; we need to search the file list to see if we need to do so, and read the
; header if we do.

        lea     umd_rmdh(pc),a0         ; processing code
        move.l  a5,a1                   ; list to process
        jsr     gu_prlis(pc)            ; process the list
        beq.s   fld_nofl                ; not in list
        sub.w   #8*2,d6                 ; read something, move further in map
        subq.w  #1,d5                   ; and knock one off things to read
        beq.s   fld_done
fld_nofl
        subq.w  #2,d6                   ; next map entry
        bpl.s   fld_cdun                ; still in map, OK
        add.w   d4,d6                   ; out of map, start again at end
fld_cdun
        bra     fld_ffnl
fld_done
        rts
;+++
; This is called from the list processing routine, to read the header of
; a given file number.  If D0 is returned >0 then the header was read OK,
; if 0 then this was not the correct entry for the file number, if -ve
; the header could not be read.
;
;       Registers:
;               Entry                           Exit
;       D0                                      error/not read
;       D1      required file number            preserved if wrong entry
;       D2      length of drive name (inc. N_)  preserved
;       A1      list entry                      preserved
;       A3      stack frame                     preserved
;---
umd_rmdh
rmdreg  reg     d3/a0
        movem.l rmdreg,-(sp)
        moveq   #0,d0                   ; assume wrong entry
        cmp.w   (a1),d1                 ; correct file number?
        bne.s   fld_exit                ; no, don't read its header

fld_flfn
        move.w  d2,d1                   ; length of source drive name
        lea     mt_file+2(a3),a2        ; where to put full name
        move.l  mt_source(a3),a0        ; get pointer to source pattern
        addq.l  #2,a0                   ; point to characters in it
        bra.s   fld_cpde
fld_cpdl
        move.b  (a0)+,(a2)+             ; copy part of drive name
fld_cpde
        dbra    d1,fld_cpdl

        lea     hdr_name-fl_data(a1),a0 ; then the filename
        move.w  (a0)+,d1                ; the name is this long
        bra.s   fld_cpfe
fld_cpfl
        move.b  (a0)+,(a2)+
fld_cpfe
        dbra    d1,fld_cpfl

        lea     mt_file+2(a3),a0        ; point to start of string again
        sub.l   a0,a2                   ; it's this long now
        move.w  a2,-(a0)                ; fill in length and point to it

        moveq   #ioa.kshr,d3            ; share it
        jsr     gu_fopen(pc)            ; open it
        bne.s   fld_excf                ; ...oops

        lea     hdr_flen-fl_data(a1),a1 ; read header to here
        moveq   #hdr.len,d2             ; all of it
        moveq   #-1,d3
        moveq   #iof.rhdr,d0
        trap    #do.io                          ; read the header
        lea     fl_data-hdr_flen-hdr.len(a1),a1 ; back to standard entry
        bne.s   fld_excl                        ; ...oops
        add.l   #hdr.len,hdr_flen-fl_data(a1)   ; unfix length
        moveq   #1,d0                           ; success, go no further

fld_excl
        jsr     gu_fclos(pc)
fld_excf
        tas     (a1)                    ; flag header read (it might not be!)

fld_exit
        tst.l   d0
        movem.l (sp)+,mdvreg
        rts
        end
