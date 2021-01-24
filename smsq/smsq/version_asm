
; SMSQ Versions

	section version

	xdef	smsq_vers

smsq_vers equ	 '3.37'

	end
	    
; #&# note all lines starting with #&# are ignored by HTMLizer, so do not appear on website
; #&# (note WL : for web site insertion: all lines must end with a full stop, if
; #&#  not, the next line is supposed to be a continuation).


; 3.37	Ramdisk makes QDOS driver again (MK).
;	Fixed io.minf for dv3 to fill name with spaces as before (broken in v3.36) (MK).
;	QL-SD: Minerva fixes, QLNET support, new BASIC utilities (MK+wl).
;	Ptr_gen fixed QL Mode support, added native QemuLator support (MK).
;	Make QDOS NUL driver again (MK).
;	Fixed dead ' key on spanish keyboard for Gold card (MK).
;	Gut make channel list distinguishes between CON and SCR channels (MK).
;	Adaption for QPC2 v5 (MK).
;	Initial support for QPC QSOUND for AY sound (MK).
;	No longer add default devices if filename starts with valid device name (MK).
;	Q68 16-bit card transfer, bugfixes (wl).
;	Q40 fix for Atari partition check (wl).
;	SBasic fixed "buffer full" error in command line history (wl).
;	SMSQmulator support for udp/udd device (wl).
;
; 3.36	QL-SD allows direct formatted cards (MK).
;	Ibm keyboard : key to generate "¼" added(MK+wl).
;	Better handling of iof_xinf (for dmedium_name$) (MK+wl).
;	SMSQmulator better keyboard read (wl).
;	Fixed FDEL (wl).
;	Q68 fixed spp_par thing (wl).
;	Q68 better card handling, CARD_INIT no longer needed (wl, inspired by MK).
;	Q68 may use interrupt for kbd read (needs new FPGA programming) (wl).
;	Q40 correct handling of direct QLWA formatted CF cards (wl).
;	Q40 can use container files on partitions 0-3 (wl).
;	Q40 WIN_CHECK, CARD_DIR, CARD_CRUSH commands added (wl).
;	Spanish keyboard tables and messages added (MK).
;
; 3.35	QL-SD modified driver (MK).
;	Fixed crash in network driver (MK).
;	Added SUSJB and FDEL keywords (PJW).
;	Sbasic : string variable cannot be REPeat control variable (wl).
;	Q68 always return even address for mem request for fast memory (wl).
;	SMSQmulator better 8 bit sprite cache handling (wl).
;
; 3.34	Added SSJOB command, extended JOB_NAME to compiled programs (PJW).
;	TK II provide for ROM and standalone version (mk).
;	QL-SD updated drivers (mk).
;	Q68 WIN_FORMAT command fixed (wl).
;	Q68 better handling of SD Cards (wl).
;	Q68 and Qx0 fix time stretch (wl).
;
; 3.33	Fixed Sbasic "end of program/command line" problems (MK).
;	Fixed Sbasic "retry-in-floating point" mechanism on overflow (MK).
;	QL network improvements (MK).
;	Disable ABC keyboard by default (MK).
;	Floppy sets step for 4 drives, not 5 (MK).
;	Improved EE + WMAN for QDOS (MK).
;	Smaller UC case conversion code (MK).
;	Incorporate stand-alone TK II sources (MK).
;	QL-SD Drivers for QLWA & QLW1 container files (MK + wl).
;	Qx0 compressed ROM (wl).
;	Qx0 better handling of FAT16 formatted CF cards in ISA adapter (wl).
;	Q68 better reading of slower cards, reads writes some SD cards (wl).
;	Q68 better ssss handling (wl).
;	SMSQmulator new way to get SSSS sample size (wl).
;
; 3.32	Fixed arithmetic stack overflow on long strings (MK).
;	All goldcard versions link in external roms (wl).
;	SQRT offset in QPC & SMSQmulator patchtables fixed (wl).
;	Release removable drive after delete (MK).
;	Better network name check (MK + wl).
;	FAT16 driver for lager disks ( up to 256 MB) (wl).
;	QUBide file system driver added (wl).
;	SMSQmulator better sound & mousewheel handling (wl).
;	Version for Q68 added (wl).
;	Q40 DISP_MODE command added (wl).
;	Q40 LBA access to QXL.WIN files on FAT32 media (wl).
;
; 3.31	RPIXL works in 8 and 16 bit colour modes (dw).
;	LRESPR within procedure bug fixed (mk).
;	Home thing uses correct job ID when opening the dir for a file (wl).
;	SMSQmulator uses "java control" Thing for misc operations (wl).
;	The string slicing bug fix in v. 3.28 is cancelled, reverting back to old behaviour (wl).
;
; 3.30	QPC: Fixed 3rd slice of mod table, start at $10000 and not $fffc (MK).
;	SMSQmulator : link in floppy module (was broken in 3.29 - wl).
;
; 3.29	DMEDIUM_DRIVE$ fixed (MK).
;	INPUT buffer is limited to max string length (32K -2) (wl).
;
; 3.28	Index of string_length +1 in string slicing before the TO no longer accepted (MK).
;	FSEND_EVENT function added (wl).
;	SMSQmulator : JVA_VER$ etc added, better stipple handling, copy QL screen (wl).
;
; 3.27	Max string array size is 32766, not 32767 (wl).
;
; 3.26	Implemented RENAME support for the DOS device (MK).
;	Added alpha blending support for general graphics functions (MK/wl).
;	New trap iow.salp to set the alpha blending weight (MK).
;	New SBasic keyword ALPHA_BLEND to set the alpha blending weight (MK).
;
; 3.25	Fixed crash for slightly overlapping windows (MK).
;
; 3.24	CKEYON/OFF no longer block jobs other than job 0 (wl).
;	When jobs are created, the size of the job, and the data space, are made even (wl).
;	Recent thing implemented (wl).
;	JVA_SCRUPDT & JVA_MBAR_STATUS for SMSQmulator (wl).
;	Cosmetic changes for smsqeweb (pjw).
;
; 3.23	DMEDIUM_DENSITY returns -1 if ramdisk, ...NAME$ returns correct name (wl).
;	SMSQmulator keyrow >8 doesn't read keyrow 0 (wl).
;
; 3.22	Added FEX_M keyword (PJW).
;	Better edge detection when moving window (wl).
;	Atari mono mode works again (wl).
;	SMSQmulator ieee float ops added (wl).
;	SMSQmulator removed debug code during initialisation (wl).
;
; 3.21	Added file open check when making directories (wl).
;	SMSQ/E DV3 floppy driver for SMSQmulator (wl).
;
; 3.20	Added read-only check in dv3_sbyt (MK).
;
; 3.19	Integrated QPC specific code (MK).
;	Fixed pending new-line bug in csize for appsub windows (MK).
;
; 3.18	Menu appsub wdws may have csizes for text objects (wl).
;
; 3.17	Window move with transparency fixed for pulled-down wdws (wl).
;
; 3.16	COLOUR_NATIVE for aurora modes fixed (MK).
;	Window move with transparency implemented (for display modes 16, 32 and 33) (wl).
;
; 3.15	Double border type fixed (MK).
;	Fixed 3d border with zero width (MK).
;	In some pointer ops, save A3 during sms.rchp for QDOS systems (MK).
;	Fixed mode 8 for recolour (MK).
;	Fixed keyrow line 1, bit 5 ("\" on English kbds) in ibm tables (MK).
;	Fixed denormalised value on divide (MK).
;	Fixed "for i=0 to a(0,0)" loop bug  (MK).
;	Fixed uq_ssq in serio (GG).
;	Correctly handle sprites with 0 mask pointer for qxl,java (MK,wl).
;
; 3.14	Cosmetic changes, removal of "Blat" macro defns in several files (WL).
;	Correct stack compression in init_exv_asm (GG).
;	Added java "machine" in machine definitions (WL).
;	Added check for SMSQmulator when moving windows (WL).
;	Added SMSQmulator specific code (WL).
;
; 3.13	One-line SELect no longer crashes on multiple expressions (MK).
;	Function()(x) no longer gives wrong char if x = last char index (MK).
;	If necessary, INPUT returns buffer overlow for compiled programs (MK).
;	Fixed swapped CTRL+SHIFT+Y and CTRL+SHIFT+Z on German keyboard (MK).
;	Fixed return value for sliced strings (MK).
;	Enable QPC numlock. Provide entry to set numlock for QPC (MK).
;	Shadow drawing bug (introduced in 3.10) fixed for Aurora (MK).
;	New system variable sys_xdly (delay after executing a job in 1/50s) (MK).
;	All inbuilt job executions respect sys_xdly (MK).
;	QPC initialises sys_xdly with 5 (MK).
;	New CTRL+C behaviour introduced, uses least-recently-used job order (MK).
;	New CTRL+C behaviour made configurable in QPC (MK).
;	Background I/O made configurable in QPC (MK).
;	Correct length set in home dir if simple device (wl).
;	New keywords YEAR%, MONTH%, DAY% and WEEKDAY% (MK).
;	QA.OP/QA.MOP load/store fixed for op-codes below $80 (MK).
;	Working QXL keyboard LEDs (BC).
;
; 3.12	HOME thing bugfixed and integrated: it is a new module (MK + wl).
;	Boot filename is set as current SBasic filename and Home filename (MK).
;	Sbasic sets home dir upon (q)load (wl).
;	File name from QD is set as home file name when using SBAS/QD (MK).
;	Improved Qx0 cache handling (TG).
;	Improved History device: Keeps track of number of message, disallows empty string (MK).
;	History device returns number of messages as file length & file pointer is limited to nbr of message (MK).
;	Basic has a new command line history (up/down arrows) (MK).
;	When display size decreased, Basic windows will be reset to within screen (MK).
;	Restore screen when unmanaged window is altered/closed (MK).
;	Screen background drawing even if window is partly buried (default OFF) (MK).
;	New commands PE_BGON and PE_BGOFF to enable/disable background drawing (MK).
;	New commands PEEK_F, PEEKS_F, POKE_F, POKES_F (MK).
;	Tries to release a Drive Definition Block if drive table is full (MK).
;	JOBID added (PW).
;
; 3.11	Bugfixes (thanks to MK).
;	Call to HOME thing via EXxx commands added (wl).
;
; 3.10	Bugfixes: lower right corner of high colour shadows fixed (MK).
;	Wrong errror return of wm.jbpla fixed (MK).
;	Double 3D border type fixed (MK).
;	Wrong error sprite return fixed (wl).
;	Sprite clipping at window borders implemented (MK).
;	Implemented sprites with no (= solid) mask (MK).
;	New copy sprite vector added (MK).
;
; 3.09	HOT_GETSTUFF$ added (MK).
;	Qx0 code is ROMmable again (wl).
;	Qx0 cachecontrol module revamped (wl).
;	No directory drive nbr > 8 allowed (wl).
;	QXL version bugfixes and restart possibility (BC).
;	QPC new basic commands (QPC_WINDOWSIZE, PAR_GETFILTER, PAR_SETFILTER) (MK).
;	Outline move spurious outline problem fixed (wl).
;
; 3.08	QPC internal version only : new basic commands (PAR_DEFAULTPRINTER$, SER_GET.
;
; 3.07	New keyword EX_M use like EX, but the job created is owned by the calling
;	job (wl).
;	Q40 flp density is REALLY preset to High (1.44 MB disks). If you want to
;	  format other density disks, set the density to D or E. (wl).
;	QXL version allows you to determine which PC drive is win1_ etc... (BC).
;	QPC SMSQ/E sampled sound system (SSSS) support (MK).
;	QPC small fix in FORMAT of removeable WIN drives (MK).
;
; 3.06	The "Quit" keyword takes an optional parameter (long int) that is
;	   passed to the calling job (eg return_code=FEW(your_program)) (PJW).
;	Q40 flp density is preset to High (1.44 MB disks). If you want to
;	  format other density disks, set the density to D or E. (wl).
;	Sprites (of 6x10 size) may be used as cursor (wl).
;
; 3.05	Corrected QL mode 8  sprite cache handling (JG).
;	Rptr distinguishes better between loose items & appsub menu items (wl).
;	Spp open clears CD inactive count (MK).
;	Better 3d border handling (MK).
;
; 3.04	Fixed pointer save on new move operation (wl).
;
; 3.03	WM_BLOCK prodecure fixed (GG & MK).
;	Some bugfixes for SER PAR PRT ports (MK)
;	PRT_USE$ used wrong offset - fixed (wl).
;	Cursor move if one-line wdw fixed (delete still a problem) and added
;	  ...configurable key to enter line into stuffer buffer (wl).
;	PARNAM$ and PARSTR$ fixed (wl).
;	EXTRAS lists all keywords, not only those it thinks are not in ROM (MK).
;	new Cachemode module for the Qx0 (FD, based on code donated by Mark Swift).
;	Extended colour border call with 0 border bug fixed (MK).
;	(Super) Goldcard initilisation problem fixed (MK).
;	Left & right shift keys are handled separately (MK).
;	Mode screen aspect ratio is variable (MK).
;
; 3.02	Extended revamp for mode 16 (Gold Card/ Aurora). (MK)
;	Bugfix for rchp : Doesn't break free space list if block was already
;	  free (MK).
;
; 3.01	More system sprites (MK).
;	New window move routines & Config block (WL + MK).
;	Fixed bug for stippled borders in WMAN (WL).
;	More con vectors (WL).
;	Bugfix for object drawing & wman rptr routines (WL & MK).
;	Fixed wallpaper on display change, broken in 2d99 (MK).
;	Bugfix for move routine in mode 4 (WL).
;
; 3.00	Fixed bug in screen driver that could crash the machine in low memory
;	    situations (MK).
;	Added many sprite modes (mode 64, mode 32/33 etc.) (MK & JG).
;	Added support for large sprites (MK).
;	New high colour WMAN with lots of new features (MK & WL).
;	New sprite formats with alpha blending & RLE compression (MK).
;	Better handling of hard disks for Q40 (JG).
;	Better handling of system sprites, many more system sprites introduced (MK).
;	WMAN colour palettes introduced & config block for them (MK).
;	New keywords FEX etc... (PW & MK).
;	Default keyboard may be different from default message languages (WL).
;	Sprites may be drawn differently depending on item status (WL + MK).
;	New con/ptr vectors concept introduced	(MK).
;	Better handling of background colours (MK).
;	Basic procs OK even if a LARGE number of parameters are passed to them (MK).
;	Bugfixes for some EE traps, keep A1 as per documentation (WL).
;	Qx0 caches on at startup (FD).
;	8 bit Aurora GD2 screen driver, also introduced in QPC (MK).
;	Screen aspect ratio for graphics functions now a CON variable (MK).
;	Hotkey system configurable again like standalone version (MK).
;
; 2.99	RND no longer returns invalid floating point for RND=0.
;	A3 addressing problems in pointer open routine fixed.
;
;    d	Fixed free/total sector info of STAT and DIR for large drives (MK).
;	Re-implemented sms.frtp the way it was supposed to be (MK).
;	Changed FREE_MEM SBASIC function to use sms.frtp (MK).
;	Background colour now drawn on-the-fly instead of consuming memory (MK).
;	Changed RAM disc for fast memory use (MK).
;
;    e	QPC implemented "fast" memory (MK).
;
;    f	EXTRAS command changed to show all SBASIC commands no matter where
;	  their code is located (MK).
;
;    x,y,z better handling of shadows in high colour mode (MK).
;	"Fast memory" also on Qx0  (MK).
;	Italian language support (FD).
;	No more MOVEP for Qx0 (FD).
;
;
; 2.98	Q40 (and other SPP common drivers) channel name for ser H/I corrected.
;	Q40 ... SER_FLOW corrected.
;	Push unnormalised FP corrected.
;	Revised init - soft RESET / hard keyboard RESET for all versions.
;
; 2.97	IBM keyboard / keypad / keyrow / tables updated.
;	Atari diskette format density selection corrected (bug since 2.93).
;	Gold Card Test version.
;
; 2.96	First Atari ST/TT GD2 version.
;	Length of negative integer to string conversion fixed again.
;	Consolidated Q40 V2.95 test versions a to e.
;	Atari select drive 1/2 for stopping drive corrected (error since 2.93).
;	Single sided formatting restored.
;	Atari disk change density detection timout increased.
;	Keypad ENTER creates DO event on all extended keyboards.
;
; 2.95	Length of negative integer to string conversion fixed!!!.
;	GD2 - Spurious D0 return from pan/scroll by zero fixed.
;	GD2 - Pan with stippled paper fixed for extended colours.
;	Another job / pointer event fix fix.
;	SSSS free queue length corrected.
;
;    a	Q40 translation for instructions > 16 MB corrected / removed.
;
;    b	Q40 DISP_SIZE checks > 512x256 not >= 512x256 for large screen.
;	Q40 More rigourous initialisation (does not assume reset).
;
;    c	Q40 DISP_SIZE size check not lost on QL => 16 bit change.
;	Q40 sms.dmod sets d2 = 0.
;	Q40 ROM reset vector restored.
;	Q40 transparent translation set for ROM .
;	>4Gb drive truncation corrected.
;	When reformatting a partition that has changed size and the new
;	    format has fewer allocation sectors and the new number of
;	    allocation sectors is odd, the free space linked list is now
;	    correctly terminated.
;
;    e	Q40/60 FPU/MMU detection corrected.
;	Q40 COM interrupts not masked on Q40 motherboard!.
;	Q40 LPT interrupts masked in MultiIOchip and interrupt scanner.
;	Q40 Machine/Processor transferred to system variables.
;	Q40 serial mouse re-enabled after mouse buffer full.
;	Q40/QXL Insert key coded as Shift Space (was CTRL F6).
;	GD2 modified to set $18063!
;
; 2.94	First extended colour version.
;	Hit not thrown away by certain button event routines (QPAC2)
;	  - bug introduced in V2.93 when trying to fix Job events.
;	FP FOR loop rounding corrected for end point >= 256 x step.
;	IDE drives format fixes for spurious bad sectors.
;	IDE drive limited to < 4 Gbyte (> 4G requires new format).
;	Writethrough cache disable code reordered.
;	Q40 MMU set up for QLiberator.
;	IDE and PC floppy disk timeouts measured during init.
;	Select on expression compiler bug fixed.
;
; 2.93	Q40 border width truncated to byte.
;	Q40 stipples 1 and 3 reversed.
;	V2.92 Q40 changes for floppy disk driver carried into other versions.
;	DV3 length check on direct sector read / write.
;	Events not thrown away by calls to WM.RPTR within WM.RTPR (eg WM.CHWIN).
;	PIPE open channel ID xxxx0004 not confused with directory (I hope).
;
; 2.92	First Q40 version.
;	Revised pointer scheduler routine.
;
; 2.91	Assign to slice of string array repaired (damaged in 2.89).
;	Selective suppression of active keystrokes.
;	Euro symbol added to fount.
;
; 2.90	In-line selects can have an explicit end select.
;	In-line defines can have an explicit end define (V2.89).
;	Thing use does not smash D3 before TH_USE call (V2.89).
;	Dragging the dursor with wake speed 0 does not lock up.
;	Atari wait for HDD ready abandoned (test cmd crashes some disks).
;
; 2.89	Ptr V1.72 Job events handled even if pointer is invisible.
;	Offscreen event (actually edge of screen) only gives out of window
;	   if the job is interested in offscreen events.
;	Thing use can be called from supervisor mode.
;	Scheduler routines called with exception vector as for job 0.
;	High priority intensive calculation tasks no longer hog the machine.
;	Atari USA keyboard table.
;	Language dependent selection works even when there are no preferences
;	   defined for the languages.
;	PIPE name and channel tied up on close.
;	File error on SAVE without filename correctly handled.
;	In-line defines accepted.
;	ED improvemnts for line numbers >32757.
;	Assigment to a slice of a parameter that is a string slice.
;
; 2.88	D7 passed through to WM.RPTR action routines.
;	Out of window flags cleared when sprite is changed.
;	PAN in mode 8 truncated to even values.
;	French keyboard driver (with "normal" number keys) for SuperHermes (UK).
;	Aurora monitor detect corrected.
;
; 2.87	Atari load can load into QVME memory.
;	Extended SCSI commands used for disks >1 Gbyte.
;	Dummy fileinfo accepts any non-zero key for execution and .bas .sav.
;	QDOS executables stored on MSDOS format disks with (invisible) .EXn
;	MSDOS files with the last char of the name '_' accessible.
;	Overwrite MSDOS file with zero length file fixed.
;	Opening SBASIC default channel 0 does not destroy messages.
;	Gold card format with no disk returns error more quickly.
;
; 2.86	INT vector corrected for small negative numbers.
;
; 2.85	UT.WTEXT modified (should be more compatible with QDOS version under
;	error conditions.
;	More changes to GOLD card init - should now recognise Aurora.
;
; 2.84	PTR 1.71 RPTR presets sub window number etc., to -1.
;	Aurora max line table corrected.
;	Some changes to GOLD card initialisation.
;
; 2.83	WMAN 1.53 underscore checks improved.
;	PTR 1.70 pointer events not eliminated when pointer is out of window.
;	Gold card initialisation trace permanently incorporated.
;	SuperGOLD card 12H/24H clock code modified.
;	Trap init bug corrected (original bug, could have strange effects).
;
; 2.82	Send event only reschedules if the target job has issued a wait event.
;	for the event and has a higher priority.
;	All trap #1s initialised, even the dummy at the end.
;
; 2.81	WMAN 1.52 job event handling (better).
;	GET string into string array does not smash SBASIC data area.
;
; 2.80	WMAN 1.51 job event handling. Aurora support.
;
; 2.79	Error returns from CN.HTOIx and CN.BTOIx made standard.
;	PIPES can be DELETEd and DIRed.
;	SCSI (Atari TT) boot config fixed.
;	NULx x now correct in channel name.
;
; 2.78A Hotkey buffer increased to 256 bytes.
;
; 2.78	Job functions (JOB$,  NXJOB etc.) do not access address if bad job id.
;	EPROM load always returns d0=0.
;	DOS format medium name works even after disk has been scrumpled by W95.
;	HD select ignored for STs without HD select.
;	Unamed pipe name includes channel number.
;	GOLD card can be configured to ignore QIMI.
;
; 2.77	Iofp.off flag implemented in iof.posa (direct sector).
;	sms.cach returns cache status in d1.l (=0 if no cache or disabled, =1
;	     if enabled) and does not set the cache if d1.l -ve.
;	Rename file to zero length name fixed.
;	Zero length substring parameter bug fixed.
;	Trap #2, ioa.cnam implemented for channel name.
;	ioa.cnam implemented in HISTORY, PIPE and SER/PAR/PRT.
;	QXL auto speed detect for network.
;	Make_dir of file xxx__ or xxx___ does not create recursive directories.
;	Timeout on ACSI bus recovery corrected.
;
; 2.76	Pointer positioning iop.sptr improved - problem since 2.71.
;	QXL Auto speed detect for network.
;	Data cache cleared on privilege violation (MC68030 problem).
;	Automatic scroll on CSIZE direction corrected.
;
; 2.75	Gold card. Rubbish character transmit on SER close fixed.
;		   This problem could also have caused spurious overwrites.
;	SB.GETx vector sets d1=0 rather than preserving the value (downgrade).
;	DIV and MOD negative handling corrected.
;
; 2.74	Atari auto partition check on boot faster if there is no ASCI target 0.
;	Window size checks corrected.
;	Overflow handling on i%+J%<0 improved.
;	Cursor limited to even x positions in mode 8.
;
; 2.73	SER_CDEOF corrected for SCC ports (Atari Mega STE / TT).
;	Set date corrected on QXL (it is still one second wrong on some PCs).
;	38400, 76800, 83333, 125000 and 153600 baud rates available on Atari,
;	    Mega STE and TT SER2.
;	ASCI error recovery improved. Auto detection of removable (CONFIG).
;	SCSI auto detection of removeable (IBM drives!!).
;	Leap year initialisation corrected.
;	Format protection on WIN devices (WIN_FORMAT).
;	DMEDIUM functions added.
;	PRINT_USING currency format extended (integer scaling more flexibility).
;	HOT_STUFF of null string fixed.
;	Handling access faults on PEEK and POKE improved
;	Name length check in DIR etc.
;	G not accepted as hex digit.
;	sb.putp OK on zero length string!.
;
; 2.72	Reliability problem with PRINT ! (V2.71) fixed.
;	Reliability problems with off screen event (V2.71) fixed.
;	IOB.SMUL calls on odd byte boundary do no cause problem for history.
;
; 2.71	Large positive error codes ignored by sms.mptr.
;	PRINT !, separators correct after word wrap.
;	DEVTYPE does not return "end of file" if file at end of file.
;	An ADD.W changed to ADD.L in EX.
;	Name length checked on rename.
;	Format flp3_1 on QXL does not format WIN1!.
;	Pointer event 6 is now "pointer off screen" (actually on edge of screen).
;	(TEST) Job events introduced (sms.sevt sms.wevt SEND_EVENT WAIT_EVENT).
;	(TEST) Job event vector mapped to bits 31-24 of pointer event.
;	ED if last line has syntax error, scroll is safe (does ESC!).
;
; 2.70	DEV_USE n   DEV_USE n,''   clear dev_use.
;	SER2 Mega STE restored (problem since TT SER2 upgrade 2.69).
;
; 2.69	QXL SDATE problems (new comms V2.60) (prot_date 1 error return) fixed.
;	EX xxx_bas now works in QLib programs.
;	SER2 TT now operates at 38400.
;	SBASIC LOOP resetting error (after editing program) fixed.
;	ED without line number starts at old top line.
;	TRAP #4 cleared on failed TRAP #2 (improves error handling in SBASIC).
;
; 2.68	QL5A format floppy disk stop handling improved.
;
; 2.67	LIST #n where n does not exist does not SAVE.
;	#0, #1, #2 all initialised even if not open (problems with WMON etc.).
;	Pause at start of SMSQ_GOLD initialisation.
;	QXL Format fixed (problem introduced in new comms V2.60).
;
; 2.66	More wait in PC for QXL.
;	a=b%*c% fixed where a>32768.
;
; 2.65	QXL Mode 8 and disp_update>1 fixed (horrible 80x86 instruction set!).
;
; 2.64	Completely re-designed QXL - PC coms.
;
; 2.63	Re-timed QXL communications.
;
; 2.62	Memory allocation on compile corrected (error in break check in 2.59).
;	X and Y saved positions in channel block swapped.
;
; 2.61	Experimental ASCI (Atari) driver.
;	Case handling of accented characters added to DIR, WCOPY etc.
;	BP.INIT (SB.INIPR) upper / lower case handling improved.
;	IOP.SVPW if fails, returns d1=0 and releases any memory allocated.
;
; 2.60	TKIII ROM ignored on Super Gold Card.
;	CMD$ set up as string so that CMD$(n) can be accessed.
;	l = st$(0) equivalent to l = LEN(st$) for string variables.
;	Allocate memory in heap rejects negative values.
;	WMAN V1.49 more the 4096 rows / columns (maybe).
;	IO retries work even if there is only one channel.
;	Re-organised QXL setup and communications - may cause problems.
;
; 2.59	IOB.ELIN returns cursor position on OK.
;	Atari HDOP for BEEP does not smash A1.
;	INPUT clears cursor on break.
;	BREAK is checked on SBASIC procedure call.
;	Error message on incomplete define (and posibly incomplete if).
;	GOSUB Expression fixed.
;	QWA format (WIN) truncate returns d0 = 0 if no sectors released.
;	QWA format checks update / random number on disk change.
;
; 2.58	OUTLN with no params OK if already outlined.
;	Changes (improvements?) to interaction between keyboard and pointer.
;	F10 (shift F5) stuffs current line in ED.
;
; 2.57	Cursor positioning clears pending newline even if position impossible.
;	    So - pending line cleared on INPUT even if line empty.
;	QXL SDATE does not get the time wrong by one minute if seconds = 0 or 1.
;	Pointer self pick improved for keyboard input.
;	Second attempt to deal with rogue drives on Gold card.
;	CALL LRESPR tidied up to improve QREF output.
;	SB_QLIBR set in all SBASIC daughter jobs ('$$external' compatibility).
;	Integer FOR range overflow fixed.
;	DATE, DATE$, DAY$, SDATE all accept 0 (current), 1 (date), 5 or 6 pars.
;	Out of memory handling changed in SBASIC.
;	SBASIC recursion beyond about 4000 levels is no longer a problem.
;
; 2.56	GOLD card: the "more delay" in 2.55 corrected.
;	Cache suppressed time increased to 5 ticks.
;
; 2.55	Atari D (49) KBD CTRL SHIFT 2 produces back arrow.
;	Gold Card (GB) KBD CTRL SHIFT ~ produces $1e.
;	QD5 Thing compiler error line number corrected.
;	EOF renamed EOFW (waits for data / end of file).
;	EOF (SuperBASIC compatible) does not wait, may miss end of file.
;	iob.suml = 6 implemented as untranslated version of iob.smul.
;	UPUT is untranslated (no TRA, no LF/CR conversion) version of BPUT.
;	Yet another attempt at Super GOLD Card plug in initialisation.
;	More delay in SuperGold card serial output.
;
; 2.54	>1023 files in directory fixed.
;	Minor change to ACSI driver.
;	QA.OP (raise FP to INT) error return corrected (used to return exp).
;	>256 kbyte files on >512 Mbyte disk scatter load corrected.
;
; 2.53	SBASIC procedure initialisation leaves some extra spare space.
;	Use of string variable as FOR or REPEAT LOOP index corrected.
;	SELECT ON string correctly trapped.
;	D3 MSW clear on poll and scheduler routine calls.
;	SMS.HDOP does not alter A3.
;	GOLD Card MT.IPCOM emulation corrected for send nothing.
;	RANDOMISE made random.
;	RETURN stack handling modified (PARNAM$ PARSTR$).
;	10 year old bug in handling names as procedure parameters fixed.
;	UT.WTEXT now smashes D3 (it was preserved)!
;	IOB.EDLN on CON device does not give "buffer full" for single line edit.
;	LOOP variables not cleared, but recreated after "EDIT", "LRESPR" etc.
;	WMON, WTV do not set outline (but still move it if set) - use OUTLN.
;	OUTLN (no params) outlines #0 to area occupied by job (window unchanged).
;	IOW.OUTL with size=-1 outlines current hit area (window unchanged).
;	In place string array element assignement corrected (error since V2.29).
;
; 2.52	EPROM_LOAD works on (Super)Gold card even if no ROM present.
;	ROM messages written out on SuperGOLD card.
;	DV3 fixes for DOS sub-directories with more than 30 files.
;	DV3 fix for rename in directory with name ending with '.'.
;	N response to COPY, OVERWRITE query does not now return error code.
;
; 2.51	GOLD Card only.
;	BAUD / Serial port changeover interaction fixed (losing characters).
;	RESET for Miracle Hard disk.
;
; 2.50	"Gold" preserved at address $1000a for Falkenberg Disk interfaces.
;	Super GOLD card links in extension cards.
;	. replaced by country code in version number on Gold V3 and Super Gold.
;	HOT_THING1 added as alternative name for HOT_WAKE.
;	SER2 available on QXL even if SER1 is not.
;	BAUD on QXL effective immediately even when SER channel is open.
;	Paragraph sign ($15) added to IBM printer translate.
;	CHAR_DEF address/0/-1, address/0/-1 (as CHAR_USE) sets default founts.
;	iow.font sets default founts if D2='DEFF'.
;
; 2.49	Further increases in ACSI timeouts.
;	QXL USA language preferences corrected.
;	Atari format OK with odd FLP_TRACK.
;	Soft reset (CTRL ALT SHIFT TAB) repaired on TT (and fast RAM 030 cards).
;	STE / TT SER2 and SER4 reliablility improved (up to 120 kbaud Mega STE).
;	Multiple ranges allowed in DLINE.
;	All ROM addresses scanned on SuperGold Card.
;
; 2.48	DEV_USEN nnn sets the name of the DEV device.
;	DEV_USE accepts single name parameter or no name = DEV_USEN.
;	ABC keyboard: F5 cancels F5.
;	Atari SCSI select timeout increased from 0.5 to 2 seconds.
;	Atari ACSI and SCSI parameter byte timeout increased to 2 seconds.
;	SER_PAUSE port, n (us) for Gold card stop bits.
;	RENUM / DLINE within procedure fixed.
;
; 2.47	SMS.EXV (MT.TRAPV) returns made QDOS compatible, SMS_ERTB correctly set.
;	Revised OPEN_DIR (sets name to true directory).
;	Revised initialisation / config for QVME.
;
; 2.46	Two changes in V2.44 did not actually appear in the release version.
;	Here they are: KEYROW and MERGE.
;	Third attempt at ABC keyboard.
;
; 2.45	Second attempt at ABC keyboard.
;	CTRL ALT SHIFT TAB is quick reset.
;	Increased delay on Gold card serial transmit.
;	CAPSLOCK does not affect ALTKEYS on any /E version.
;
; 2.44	X=0-2^N is now normalised.
;	Keyrow access to address 2 fixed (or should have been - see V2.46).
;	More DOS filename friendly:
;	  . accepted in SBASIC name;
;	  _ accepted in DOS name.
;	DOS second map update corrected.
;	DISP_UPDATE added to QXL SMSQ/E.
;	Faster detection of no disk (very much faster on GOLD card).
;	RES_128 and RES_SIZE now read boot file.
;	Fast RAM accepted for any ST.
;	CONFIG level 2 and configurable inital screen size for Atari.
;	SHIFT TAB at start of line fixed.
;	MERGE does not change SBASIC program name (or should not - see V2.46).
;
; 2.43	New loader for GOLD Card version 3.
;
; 2.42	Error message numbers -(n*128) give unknown error message.
;	WGET, WPUT, LGET, LPUT, HGET, HPUT added.
;	QVME pan top lhword of screen OK.
;	All module titles checked.
;
; 2.41	INPUT (and program input) can survive buffer >32767.
;	First try at ABC keyboard interface.
;	=value outside select clause is correctly trapped.
;	TT set clock does not overwrite (a2).
;	Atari FLP cache handling corrected.
;
; 2.40	ED - Extended delete back now works reliably across multiple lines.
;	   - Edit at bottom of window is now safe(r).
;	First E version for QXL.
;	HOTKEY (and BT_HOTKEY) amended for HOT_CMD.
;	UT.CSTR corrected for cases 0 and 1 (QUBIDE).
;	FIX for SERMOUSE on GOLD Card.
;
; 2.39	PROT_MEM / PROT_DATE for unmodified Atari ST repaired (bad since 2.36).
;	PROT_MEM for unmodified Atari ST more fully implemented.
;	CAPSLOCK implemented on GOLD and SuperGOLD card.
;	GOLD and SuperGOLD card ED handling corrected.
;	UT.CSTR available on SuperGOLD card and nearly right (see V2.40).
;
; 2.38	Fixes two character wrap problems in IOB.ELIN (one introduced in 2.37).
;	Super GOLD card configuration and BOOT corrected.
;
; 2.37	QSAVE does not lose a line for every WHEN ERRor.
;	Limit check on sub-strings of string arrays corrected (bug since V2.34).
;	CURSOR with four parameters can go outside window.
;	CALL from Turbo programs fixed (problem since QLOAD was added in V2.22).
;	WIN_DRIVE$ rejects 0 as parameter.
;	RENUM 1,1 accepted.
;	Type left in SBASIC procedure and function definitions (QREF).
;	4 floppy disk drives supported on GOLD and SUPERGOLD cards.
;
; 2.36	Second attempt at Super GOLD Card version.
;	LOAD program, DLINE TO, MERGE similar program: does not now fail.
;	Further improvements to QXL keybard handling.
;	Atari SER2 and SER4 receive special condition directed to receive code.
;	ED can cope with more than 57 lines in window.
;	GOLD Card SER driver adapted for SERmouse.
;
; 2.35	MODE 8 open CON sets CSIZE 2,0 (not 0,1 as before!).
;	NET Server does not screw up QXL interrupt server (as much!).
;	QXL Keyboard (and SER) made more immune to interrupt disabling.
;	QXL Keyboard status lights (sometimes) reflect internal QXL status.
;	DDOWN (etc) do not return silly error code.
;	MERGE and MRUN within programs fixed.
;	Pointer Interface CTRL C handling made more robust.
;
; 2.34	String slice longer than undimensioned string is truncated (see V2.37).
;	Defaulting improved for case where file exists (returns already exists).
;	ACSI key added to TT version of sms.achp.
;	% and $ not allowed in names.
;	Hermes Baud corrected.
;	New QXL.EXE with serial comms improved for Duplex operation.
;
; 2.33	First GOLD card version.
;	Error on SAVE overwrite - N corrected.
;
; 2.32	SB.GTLIN corrected for string to long integer conversion.
;	EX basic program immunised against $4AFBs left behind by QLiberator.
;	"Unset" variables now have name usage 0 (like SuperBASIC)
;		- RJOB etc. restored to old style.
;	SAVE (noname) after LOAD of a _sav file automatically adds _bas.
;
; 2.31	Rework of defaulting strategy - fixes QLiberator channel #0 problems!.
;	INPUT (etc) a$	where a$ is an array element, clears a$ to spaces.
;	'12345678' (2 to 5) (2 to 3) now accepted.
;	Internal modification of check write protect: should be invisible.
;
; 2.30	Float and integer FOR loops with string ranges fixed.
;	Atari ST HD and precompensation strategy changed.
;	Assignment to substring parameters permitted.
;	FILL$ checks for length in range.
;	SAVE PAR does not give a spurious error.
;	Procedure call mechanism compatible with Q_ERR_ON.
;
; 2.29	String slices may be longer than strings.
;	Assigning strings to elements of a string array clears the element.
;	DATA statement handling in erroneous programs correct (it looked like
;	    a compiler or ED problem).
;	'sz' and greek letters treated as letters.
;	RJOB etc. accept name as a job name.
;
; 2.28	BREAK with job 0 asleep no longer crashes.
;	HOT_KEY and HOT_CMD accept null parameters.
;	Buried job 0 does not lose error messages (so often).
;	WIN_WP acts immediately.
;	Channel and Job tags always positive.
;	KBD_TABLE address made consistent with old SMSQ / old Atari drivers.
;	TRAP #3 with d3<>0 in supervisor mode are no longer atomic.
;	INPUT buffer made expandable.
;	IOB.TBYT IOB.FBYT permitted on directory (EOF now works).
;	0^0 is 1!!
;	QXL network repaired.
;
; 2.27	ED: ALT backspace within line number fixed.
;	SYS_LANG moved to avoid QSOUND conflict.
;	QD5 Thing extended to handle CLEAR, CONTINUE/RETRY and WHEN ERROR.
;	String arrays filled with spaces, TO n allowed up to dimension (arith).
;	Out of memory on dimension is non-fatal.
;	Listing of $0 corrected.
;	MODE of high res QL displays is now 0 or 8 (was 1 or 9 internally).
;	Blitter included in MACHINE type.
;	Channel ID corrected in built in OUTLN.
;	HISTORY OPEN preserves A0!!
;	Check for incorrect filetype on LOAD is fixed.
;	Atari DV3 sets defaults to WIN if WIN1_BOOT is found.
;	SBASIC error message handling clears Trap #4.
;	SBASIC error recovery from errors in command FILE improved.
;	Check for dangling NEXT, EXIT and END in command line improved.
;	INSTR_CASE added.
;	Stack overrun on string slicing fixed.
;
; 2.26	FORMAT and immediately access disk problem fixed (Atari).
;	Extensions may be added in a loop.
;	Editing programs with integer loop variables fixed.
;
; 2.25	SBASIC release version: from now on, ALL corrections will be recorded.
;	Fixes for DOS format make directory and delete;
;		  null sub-strings of string arrays as parameters.
;		  Assign string variable to integer.
;		  FOR and REP loops cleared.
;
; 2.24	First full TT version.
;
; 2.23	'Release' Version?.
;	Turbo compatibility restored.
;	POKE_W accepts oversize numbers.
;	SBAS/QD Thing added.
;	FileInfo Thing added.
;
; 2.22	A few errors corrected (including MAP overwrite).
;	Cache suppressed for 2 ticks only.
;	Rationalise cursor key handling.
;	HISTORY added.
;	QSAVE QLOAD etc. etc. (see V2.37)
;
; 2.21	BASIC VARS in low memory, staging posts for vectors.
;	New functions and procedures completed.
;
; 2.20	Atari HD floppy disks supported.
;	TRIG errors intercepted correctly.
;
; 2.19	German corrected.
;	INPUT substrings added.
;	UT_GTxxx (CA_GTxxx) handles substring arrays.
;	Delete subdirectory corrected.
;
; 2.18	Language facilities added.
;	QLWA free space handling corrected.
;	Even mare compatible DV3.
;
; 2.17	New loader.
;
; 2.16	Yet more.
;
; 2.15	Even more.
;
; 2.14	More bugs removed. Even more compatible DV3.
;
; 2.13	Lots of bugs removed. More compatible DV3.
;
; 2.12	First test SBASIC version. Multi SBASIC.
;
; 2.11	Experimental SBASIC.
;
; 2.10	Correction to screen update for VGA and SVGA.
;
; 2.09	Mods to Screen update algorithm (particularly for 800x600).
;	INSTR is 5 chars long. Printer works in 8 bit slot.
;	CON newline is held pending even if cursor is enabled (QL ROM bug).
;
; 2.08	Completely rewritten QXL/PC comms.
;	Fixes for 600x800 pixel, CN.FTOD, printer.
;
; 2.07	Fixes for communicaion loss, 800 pixel screen, date$.
;
; 2.06	QXL Floppy disk (re)format.
;
; 2.05	QXL NET device.
;
; 2.04	Improved QXL screen update checks.
;
; 2.03	First Turbo compatible Version.
;
; 2.02	First QXL Version.
;
; 2.01	Length returned by sms.achp unadjusted.
;	Job priority byte only.
;
; 2.00	First test version.
;
